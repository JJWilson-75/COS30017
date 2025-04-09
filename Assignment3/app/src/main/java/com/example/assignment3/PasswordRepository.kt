package com.example.assignment3

import androidx.lifecycle.LiveData

class PasswordRepository(private val passwordDao: PasswordDao) {

    val allPasswords: LiveData<List<Password>> = passwordDao.getAllPasswords()

    fun getPasswordById(id: Int): LiveData<Password> {
        return passwordDao.getPasswordById(id)
    }

    suspend fun insert(passwordEntry: Password) {
        passwordDao.insert(passwordEntry)
    }

    suspend fun update(passwordEntry: Password) {
        passwordDao.update(passwordEntry)
    }

    suspend fun delete(passwordEntry: Password) {
        passwordDao.delete(passwordEntry)
    }
}