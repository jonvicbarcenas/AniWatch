package com.anime.aniwatch

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.anime.aniwatch.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Set up the toolbar with a back button
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show back button
        supportActionBar?.title = null

        // Handle back button click in the toolbar
        toolbar.setNavigationOnClickListener {
            onBackPressed() // This will handle going back to the previous screen
        }

        // Signup button click listener
        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (isValidEmail(email)) { // Validate email format
                    if (password == confirmPassword) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    // Navigate to SignIn activity after successful signup
                                    val intent = Intent(this, SignIn::class.java)
                                    startActivity(intent)
                                    finish() // Close the current SignUp activity
                                } else {
                                    Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, SignIn::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    // Validate email format using regex
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        val pattern = Pattern.compile(emailPattern)
        return pattern.matcher(email).matches()
    }


    // Generate a random username
    private fun generateRandomUsername(): String {
        val adjectives = listOf("Cool", "Epic", "Fast", "Mighty", "Brave", "Lucky", "Chill", "Smart", "Happy", "Sneaky")
        val nouns = listOf("Tiger", "Panda", "Eagle", "Wolf", "Ninja", "Phoenix", "Shadow", "Knight", "Hawk", "Dragon")
        val randomAdjective = adjectives.random()
        val randomNoun = nouns.random()
        val randomNumber = (100..999).random()
        return "$randomAdjective$randomNoun$randomNumber"
    }
}
