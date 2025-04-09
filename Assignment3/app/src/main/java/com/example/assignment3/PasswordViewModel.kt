package com.example.assignment3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PasswordRepository
    val allPasswords: LiveData<List<Password>>

    init {
        val passwordDao = AppDatabase.getDatabase(application, viewModelScope).passwordDao()
        repository = PasswordRepository(passwordDao)
        allPasswords = repository.allPasswords
    }

    fun getPasswordById(id: Int): LiveData<Password> {
        return repository.getPasswordById(id)
    }

    fun insert(passwordEntry: Password) = viewModelScope.launch {
        repository.insert(passwordEntry)
    }

    fun update(passwordEntry: Password) = viewModelScope.launch {
        repository.update(passwordEntry)
    }

    fun delete(passwordEntry: Password) = viewModelScope.launch {
        repository.delete(passwordEntry)
    }
}