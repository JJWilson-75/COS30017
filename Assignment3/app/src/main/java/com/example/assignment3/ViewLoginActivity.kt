package com.example.assignment3

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment3.databinding.ActivityViewLoginBinding

class ViewLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewLoginBinding
    private val passwordViewModel: PasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val passwordId = intent.getIntExtra("PASSWORD_ID", -1)
        if (passwordId != -1) {
            passwordViewModel.getPasswordById(passwordId).observe(this) { passwordEntry ->
                passwordEntry?.let {
                    binding.itemNameTextView.text = it.itemName
                    binding.usernameTextView.text = it.username
                    binding.passwordTextView.text = it.password // Or mask it as "********"
                    binding.websiteUrlTextView.text = it.websiteUrl
                    binding.noteTextView.text = it.note
                }
            }
        }

        // Handle edit button click
        binding.editButton.setOnClickListener {
            val intent = Intent(this, EditLoginActivity::class.java)
            intent.putExtra("PASSWORD_ID", passwordId)
            startActivity(intent)
        }
    }
}