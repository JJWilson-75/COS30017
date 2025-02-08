package com.example.assigment1

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var trackingScore: Int = 0 //score to keep track
    private var trackingHold: Int = 0 //keep track of hold user is on
    private var hasFallen: Boolean = false

    override fun onStart() {
        super.onStart()
        Log.i("LIFECYCLE", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("LIFECYCLE", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("LIFECYCLE", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("LIFECYCLE", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("LIFECYCLE", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("LIFECYCLE", "onRestart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("LIFECYCLE", "onCreate") //Log when create activity

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val climbButton = findViewById<Button>(R.id.climb_button)
        val fallButton = findViewById<Button>(R.id.fall_button)
        val resetButton = findViewById<Button>(R.id.reset_button)

        val score = findViewById<TextView>(R.id.score)

        score.text = trackingScore.toString() //May cause bugs later on

        savedInstanceState?.let {
            trackingScore = savedInstanceState.getInt("SCORE") //this is just the KEY to get the score
            trackingHold = savedInstanceState.getInt("HOLD") //this is just the KEY to get the hold
            hasFallen = savedInstanceState.getBoolean("FALLEN")
            score.text = trackingScore.toString()
            updateButtons()
        }

        updateButtons()

        climbButton.setOnClickListener {_ ->
            score.text = climbAction().toString()
            updateButtons()
        }

        fallButton.setOnClickListener {_ ->
            score.text = fallAction().toString()
            updateButtons()
        }

        resetButton.setOnClickListener {_ ->
            score.text = resetAction().toString()
            updateButtons()
        }
    }

    //Save instance through bundle
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SCORE", trackingScore)
        outState.putInt("HOLD", trackingHold)
        outState.putBoolean("FALLEN", hasFallen)
        Log.i("LIFECYCLE", "saveInstanceState: score - $trackingScore, hold - $trackingHold, hasFallen - $hasFallen")
    }

    private fun climbAction(): Int {
        trackingHold +=1 //User has climbed one additional hold

        when (trackingHold)
        {
            in 1..3 -> trackingScore +=1
            in 4..6 -> trackingScore +=2
            in 7..9 -> trackingScore +=3
        }

        if (trackingScore >= 18) trackingScore = 18 //Additional measure to guarantee score not go over 18, may be unnecessary
        Log.i("TRACKING", "Climb, Score: $trackingScore, Hold: $trackingHold")

        return trackingScore
    }

    private fun fallAction(): Int {
        trackingScore -= 3
        hasFallen = true

        if (trackingScore < 0) trackingScore = 0 //Additional measure to guarantee score not go under 0, may be unnecessary
        Log.i("TRACKING", "Fall, Score: $trackingScore, Hold: $trackingHold")

        return trackingScore
    }

    private fun resetAction(): Int {
        trackingScore = 0
        trackingHold = 0
        hasFallen = false //reset hasFallen to false to enable climb button

        Log.i("TRACKING", "Reset, Score: $trackingScore, Hold: $trackingHold")

        return trackingScore
    }

    private fun updateButtons() {
        val climbButton = findViewById<Button>(R.id.climb_button)
        val fallButton = findViewById<Button>(R.id.fall_button)
        val score = findViewById<TextView>(R.id.score)

        when (trackingHold) {
            0 -> { //safeguard to make sure user can't fall when in hold 0
                score.setTextColor(Color.GRAY)

                climbButton.setEnabled(true)
                fallButton.setEnabled(false)
            }
            in 1..3 -> {
                score.setTextColor(Color.BLUE) //change color of score to match the zone

                if (hasFallen) {
                    climbButton.setEnabled(false)
                    fallButton.setEnabled(false)
                } else {
                    climbButton.setEnabled(true)
                    fallButton.setEnabled(true)
                }
            }
            in 4.. 6 -> {
                score.setTextColor(Color.rgb(0, 100, 0)) //change color of score to match the zone

                if (hasFallen) {
                    climbButton.setEnabled(false)
                    fallButton.setEnabled(false)
                } else {
                    climbButton.setEnabled(true)
                    fallButton.setEnabled(true)
                }
            }
            in 7.. 8 -> {
                score.setTextColor(Color.RED) //change color of score to match the zone

                if (hasFallen) {
                    climbButton.setEnabled(false)
                    fallButton.setEnabled(false)
                } else {
                    climbButton.setEnabled(true)
                    fallButton.setEnabled(true)
                }
            }
            9 -> { //safeguard to make sure user can't fall or climb when in hold 9
                score.setTextColor(Color.RED)

                climbButton.setEnabled(false)
                fallButton.setEnabled(false)
            }
        }

        Log.i("BUTTONS", "Climb button: ${climbButton.isEnabled}" + ", Fall button: ${fallButton.isEnabled}")
    }
}