package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment3.databinding.ActivityEditLoginBinding

class EditLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditLoginBinding
    private val passwordViewModel: PasswordViewModel by viewModels()
    private var passwordId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the password ID from intent
        passwordId = intent.getIntExtra("PASSWORD_ID", -1)

        if (passwordId != -1) {
            passwordViewModel.getPasswordById(passwordId).observe(this) { passwordEntry ->
                passwordEntry?.let {
                    binding.itemNameEditText.setText(it.itemName)
                    binding.usernameEditText.setText(it.username)
                    binding.passwordEditText.setText(it.password)
                    binding.websiteUrlEditText.setText(it.websiteUrl)
                    binding.noteEditText.setText(it.note)
                }
            }
        }

        // Update password entry when clicking the update button
        binding.updateButton.setOnClickListener {
            val updatedItemName = binding.itemNameEditText.text.toString()
            val updatedUsername = binding.usernameEditText.text.toString()
            val updatedPassword = binding.passwordEditText.text.toString()
            val updatedWebsiteUrl = binding.websiteUrlEditText.text.toString()
            val updatedNote = binding.noteEditText.text.toString()

            if (validateInputs(updatedItemName, updatedUsername, updatedPassword, updatedWebsiteUrl)) {
                val updatedPasswordEntry = Password(
                    id = passwordId,
                    itemName = updatedItemName,
                    username = updatedUsername,
                    password = updatedPassword,
                    websiteUrl = updatedWebsiteUrl,
                    note = updatedNote
                )

                passwordViewModel.update(updatedPasswordEntry)
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Delete password entry when clicking the delete button
        binding.deleteButton.setOnClickListener {
            val passwordToDelete = Password(
                id = passwordId,
                itemName = binding.itemNameEditText.text.toString(),
                username = binding.usernameEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                websiteUrl = binding.websiteUrlEditText.text.toString(),
                note = binding.noteEditText.text.toString()
            )

            showDeleteConfirmationDialog(passwordToDelete)
        }
    }

    private fun validateInputs(itemName: String, username: String, password: String, websiteUrl: String): Boolean {
        if (itemName.isEmpty()) {
            showToast("Item name cannot be empty")
            return false
        }

        if (!isValidUsername(username)) {
            showToast("Username must be alphanumeric and max 10 characters")
            return false
        }

        if (!isValidPassword(password)) {
            showToast("Password must be 8-20 characters, include a letter, a number, and a special character")
            return false
        }

        if (!isValidWebsite(websiteUrl)) {
            showToast("Enter a valid website URL")
            return false
        }

        return true
    }

    private fun isValidUsername(username: String): Boolean {
        return username.matches(Regex("^[a-zA-Z0-9]{1,10}$"))
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length in 8..20 &&
                password.any { it.isLetter() } &&
                password.any { it.isDigit() } &&
                password.any { "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~".contains(it) }
    }

    private fun isValidWebsite(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteConfirmationDialog(passwordEntry: Password) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this password?")
            .setPositiveButton("Delete") { _, _ ->
                passwordViewModel.delete(passwordEntry) // Proceed with deletion

                // Redirect to MainActivity after deletion
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null) // Dismiss dialog
            .show()
    }
}
