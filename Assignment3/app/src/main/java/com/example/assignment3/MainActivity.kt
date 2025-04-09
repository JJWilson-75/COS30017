package com.example.assignment3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment3.databinding.ActivityMainBinding
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val passwordViewModel: PasswordViewModel by viewModels()
    private lateinit var adapter: PasswordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        adapter = PasswordAdapter(
            onViewClick = { passwordEntry ->
                val intent = Intent(this, ViewLoginActivity::class.java)
                intent.putExtra("PASSWORD_ID", passwordEntry.id)
                startActivity(intent)
            },
            onDeleteClick = { passwordEntry ->
                showDeleteConfirmationDialog(passwordEntry)
            }
        )
        binding.recyclerViewLogins.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLogins.adapter = adapter

        // Observe the list of passwords
        passwordViewModel.allPasswords.observe(this) { passwords ->
            adapter.submitList(passwords)
        }

        // Set up New button
        binding.newButton.setOnClickListener {
            val intent = Intent(this, NewLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDeleteConfirmationDialog(passwordEntry: Password) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this password?")
            .setPositiveButton("Delete") { _, _ ->
                passwordViewModel.delete(passwordEntry) // Proceed with deletion
            }
            .setNegativeButton("Cancel", null) // Dismiss dialog
            .show()
    }
}