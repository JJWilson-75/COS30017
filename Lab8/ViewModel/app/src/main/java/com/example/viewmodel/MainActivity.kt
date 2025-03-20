package com.example.viewmodel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //var temporaryScore:Int = 0
        var buttonCounter = findViewById<Button>(R.id.button)

        findViewById<TextView>(R.id.score).text = mainViewModel.toString()

        buttonCounter.setOnClickListener {
            mainViewModel.addNumber()
            findViewById<TextView>(R.id.score).text = mainViewModel.toString()
        }
    }
}