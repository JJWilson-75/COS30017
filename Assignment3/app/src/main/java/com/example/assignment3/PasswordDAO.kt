package com.example.assignment3

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PasswordDao {
    @Query("SELECT * FROM password")
    fun getAllPasswords(): LiveData<List<Password>>

    @Query("SELECT * FROM password WHERE id = :id")
    fun getPasswordById(id: Int): LiveData<Password>

    @Insert
    suspend fun insert(passwordEntry: Password)

    @Update
    suspend fun update(passwordEntry: Password)

    @Delete
    suspend fun delete(passwordEntry: Password)
}