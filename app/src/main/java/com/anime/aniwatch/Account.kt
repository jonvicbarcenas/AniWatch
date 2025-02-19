package com.anime.aniwatch

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anime.aniwatch.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the saved email from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("userEmail", "No Email")
        binding.email.text = savedEmail ?: "No Email" // Display email as non-editable TextView

        // Load the saved username from SharedPreferences
        val savedUsername = sharedPreferences.getString("username", "Unknown User")
        binding.fullName.text = savedUsername ?: "Unknown User"

        // Load the saved profile image from SharedPreferences
        val savedImageResId = sharedPreferences.getInt("profileImageRes", R.drawable.account)  // Default image if not set

        // Set the image in the ImageView
        val profileImageView: ImageView = binding.profile
        profileImageView.setImageResource(savedImageResId)

        // Navigate to ProfileEditActivity for updating username/email
        binding.editProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivityForResult(intent, 100)
        }

        binding.security.setOnClickListener {
            val intent = Intent(requireContext(), SecurityActivity::class.java)
            startActivity(intent)
        }

        binding.settings.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }


        // Handle logout action
        binding.logout.setOnClickListener {
            showLogoutDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            // Get the updated username (if any)
            val updatedUsername = data?.getStringExtra("updatedUsername")
            val updatedEmail = data?.getStringExtra("updatedEmail")

            if (!updatedUsername.isNullOrEmpty()) {
                binding.fullName.text = updatedUsername
            }

            val imageResId = data?.getIntExtra("selectedImage", R.drawable.account) ?: R.drawable.account
            val profileImageView: ImageView = binding.profile
            profileImageView.setImageResource(imageResId)
        }
    }

    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            logoutUser()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun logoutUser() {
        val sharedPreferences = requireContext().getSharedPreferences("loginPrefs", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        editor.remove("userEmail")
        editor.remove("userPassword")


        editor.apply()

        val intent = Intent(activity, SignIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
