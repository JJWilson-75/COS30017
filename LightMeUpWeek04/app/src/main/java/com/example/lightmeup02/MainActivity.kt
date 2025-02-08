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

        val imageView = findViewById<ImageView>(R.id.imageView)

        savedInstanceState?.let{
            state = it.getInt("IMAGE")
            imageView.setImageDrawable(getDrawable(state))
        }

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("IMAGE", state)
    }
}