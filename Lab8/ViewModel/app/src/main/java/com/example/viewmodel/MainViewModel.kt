package com.example.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var counter = 0

    fun addNumber(){
        counter++
    }
}