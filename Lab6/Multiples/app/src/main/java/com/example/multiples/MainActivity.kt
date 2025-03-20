package com.example.multiples

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val factor1 = Random.nextInt(from = 1, until = 13)
        val factor2 = Random.nextInt(from = 1, until = 13)
        val tvFactor1 = findViewById<TextView>(R.id.factor1)
        val tvFactor2 = findViewById<TextView>(R.id.factor2)

        tvFactor1.text = factor1.toString()
        tvFactor2.text = factor2.toString()

        val multiply = findViewById<Button>(R.id.multiply)
        multiply.setOnClickListener {

        }
    }
}