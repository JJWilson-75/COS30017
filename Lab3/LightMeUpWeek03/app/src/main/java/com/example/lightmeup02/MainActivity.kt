package com.example.lightmeup02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    var state = R.drawable.favorite_24dp_5f6368

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setOnLongClickListener {
            state = when (state) {
                R.drawable.favorite_24dp_5f6368 -> R.drawable.info_24dp_5f6368
                R.drawable.info_24dp_5f6368 -> R.drawable.favorite_24dp_5f6368
                else -> R.drawable.favorite_24dp_5f6368
            }
            imageView.setImageDrawable(getDrawable(state))
            true //must return boolean, don't need return as this is lamdba
        }
    }
}