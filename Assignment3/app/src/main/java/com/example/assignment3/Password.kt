package com.example.assignment3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password")
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary key, auto-incremented
    val itemName: String,
    val username: String,
    val password: String,
    val websiteUrl: String,
    val note: String
)