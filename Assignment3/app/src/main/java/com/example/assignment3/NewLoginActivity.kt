package com.example.assignment3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment3.databinding.ActivityNewLoginBinding
import android.util.Patterns

class NewLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewLoginBinding
    private val passwordViewModel: PasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val itemName = binding.itemNameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val websiteUrl = binding.websiteUrlEditText.text.toString()
            val note = binding.noteEditText.text.toString()

            if (validateInputs(itemName, username, password, websiteUrl)) {
                val passwordEntry = Password(
                    itemName = itemName,
                    username = username,
                    password = password,
                    websiteUrl = websiteUrl,
                    note = note
                )
                passwordViewModel.insert(passwordEntry)
                finish() // Return to MainActivity
            }
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
}