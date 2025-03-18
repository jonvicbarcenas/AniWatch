package com.anime.aniwatch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anime.aniwatch.AccountFragment
import com.anime.aniwatch.HomeFragment
import com.anime.aniwatch.ListFragment
import com.anime.aniwatch.R
import com.anime.aniwatch.SearchActivity
import com.anime.aniwatch.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure Firebase is initialized
        FirebaseApp.initializeApp(this)

        // Check if user is logged in
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Firebase is connected and user is logged in, proceed to fetch/update data
            fetchDataFromFirebase(user)
        } else {
            // User is not logged in, show a toast and proceed accordingly
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show()
        }

        // Set up the main toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = null

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // Handle bottom navigation selection
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.home -> HomeFragment()
                R.id.list -> ListFragment()
                R.id.account -> AccountFragment()
                else -> null
            }
            fragment?.let { replaceFragment(it) }
            fragment != null
        }
    }

    // Fetch user data from Firebase
    private fun fetchDataFromFirebase(user: FirebaseUser) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(user.uid)

        userRef.get().addOnSuccessListener {
            if (it.exists()) {
                // If the user data exists, you can fetch the current data (username, email)
                val currentUsername = it.child("username").value.toString()
                val currentEmail = it.child("email").value.toString()
                Log.d("MainActivity", "User data already exists in Firebase")

                // Optionally: Update data if necessary, for example, on username change
                // For now, no need to update if no changes to username
            } else {
                // If the user data doesn't exist, write the default data
                writeDataToFirebase(user, user.email ?: "No Email", "Default Username")
            }
        }.addOnFailureListener {
            Log.e("MainActivity", "Failed to read user data: ${it.message}")
        }
    }

    private fun writeDataToFirebase(user: FirebaseUser, email: String, newUsername: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users")

        // Use the new username and the existing email
        val userData = hashMapOf(
            "username" to newUsername,  // Updated username
            "email" to email            // Retain the email
        )

        userRef.child(user.uid).setValue(userData)
            .addOnSuccessListener {
                Log.d("FirebaseTest", "Data written successfully to Firebase")
                Toast.makeText(this, "Data written to Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.e("FirebaseTest", "Failed to write data: ${it.message}")
                Toast.makeText(this, "Failed to write data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        // Update toolbar based on the fragment
        when (fragment) {
            is HomeFragment -> {
                supportActionBar?.show()
                disableBackButton()
                supportActionBar?.setDisplayShowTitleEnabled(false)
                showSearchButton()
            }
            is ListFragment -> {
                supportActionBar?.show()
                supportActionBar?.setDisplayShowTitleEnabled(true)
                supportActionBar?.title = "My List"
                disableBackButton()
            }
            is AccountFragment -> {
                supportActionBar?.hide()
            }
        }
    }

    private fun enableBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun disableBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun showSearchButton() {
        val searchButton = binding.toolbar.menu.findItem(R.id.action_search)
        searchButton?.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

