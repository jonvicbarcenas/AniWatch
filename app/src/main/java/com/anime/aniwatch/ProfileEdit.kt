package com.anime.aniwatch

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anime.aniwatch.databinding.ActivityProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private val TAG = "PROFILE_EDIT_TAG"

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    imageUri = data.data
                    binding.profileImage.setImageURI(imageUri) // Show selected image
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar with a back button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Profile"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        storageReference = FirebaseStorage.getInstance().reference

        val userEmail = auth.currentUser?.email
        binding.emailadd.setText(userEmail ?: "")

        val uid = auth.currentUser?.uid
        if (uid != null) {
            databaseReference.child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.child("username").getValue(String::class.java)
                    binding.username.setText(user ?: "")
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }

        binding.update.setOnClickListener {
            updateProfile(uid)
        }
    }

    private fun updateProfile(uid: String?) {
        if (uid == null) return

        val username = binding.username.text.toString().trim()
        val email = binding.emailadd.text.toString().trim()

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Username and Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userUpdates = hashMapOf<String, Any>(
            "username" to username,
            "email" to email
        )

        // If an image was selected, upload it to Firebase Storage
        if (imageUri != null) {
            val imageRef = storageReference.child("profile_images/${uid}.jpg")

            imageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Store the image URL in the database
                        userUpdates["profileImageUrl"] = uri.toString()

                        // Update user profile with new data
                        updateUserDataInDatabase(uid, userUpdates)
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Failed to upload image: ${it.message}")
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    updateUserDataInDatabase(uid, userUpdates) // Update without image if upload fails
                }
        } else {
            // If no image was selected, update the user data without image
            updateUserDataInDatabase(uid, userUpdates)
        }
    }

    private fun updateUserDataInDatabase(uid: String, userUpdates: HashMap<String, Any>) {
        databaseReference.child(uid).updateChildren(userUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Profile updated in Firebase: $userUpdates")
                    Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()

                    // Send updated username back to AccountFragment
                    val resultIntent = Intent()
                    resultIntent.putExtra("updatedUsername", userUpdates["username"] as String)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    Log.e(TAG, "Update failed: ${task.exception?.message}")
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error: ${exception.message}")
                Toast.makeText(this, "Database error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveProfileImageLocally(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val file = File(filesDir, "profile_image.jpg")
        val outputStream = FileOutputStream(file)

        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Log.d(TAG, "Profile image saved locally: ${file.absolutePath}")
    }
}
