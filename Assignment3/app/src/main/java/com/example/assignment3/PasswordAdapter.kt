package com.example.assignment3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PasswordAdapter(
    private val onViewClick: (Password) -> Unit,
    private val onDeleteClick: (Password) -> Unit
) : RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {

    private var passwordList: List<Password> = emptyList()

    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.item_name_text_view)
        val usernameTextView: TextView = itemView.findViewById(R.id.username_text_view)
        val viewButton: Button = itemView.findViewById(R.id.view_button)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_login, parent, false)
        return PasswordViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val passwordEntry = passwordList[position]
        holder.itemNameTextView.text = passwordEntry.itemName
        holder.usernameTextView.text = passwordEntry.username
        holder.viewButton.setOnClickListener { onViewClick(passwordEntry) }
        holder.deleteButton.setOnClickListener { onDeleteClick(passwordEntry) }
    }

    override fun getItemCount(): Int = passwordList.size

    fun submitList(newList: List<Password>) {
        passwordList = newList
        notifyDataSetChanged()
    }
}