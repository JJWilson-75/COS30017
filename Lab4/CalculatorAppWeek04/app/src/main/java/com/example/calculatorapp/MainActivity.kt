package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    var opResult: Int = 0
    var operator = "plus"

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

    //Bundle in the parameter with ? operator mean it can be null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("LIFECYCLE", "onCreate") //Log when create activity

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val number1 = findViewById<EditText>(R.id.number1)
        val number2 = findViewById<EditText>(R.id.number2)

        val equals = findViewById<Button>(R.id.equals)
        val answer = findViewById<TextView>(R.id.answer)

        //This is recommended over the other method
        //MUST comment line 111 - 117 ///override fun onRestoreInstanceState(savedInstanceState: Bundle) {...}/// to use this
        //can be shortened to savedInstanceState?.let {}
        if (savedInstanceState != null) {
            opResult = savedInstanceState.getInt("ANSWER") //this is just the KEY to get the result
            answer.text = opResult.toString()
        }

        equals.setOnClickListener{_ ->
            opResult = when(operator)
            {
                "plus" -> add(number1.text.toString(), number2.text.toString())
                "subtract" -> subtract(number1.text.toString(), number2.text.toString())
                else -> add(number1.text.toString(), number2.text.toString())
            }

            answer.text = opResult.toString()
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.plus -> if (checked) {
                    operator = "plus"
                    Log.d("RADIO", "Plus is checked")
                }
                R.id.subtract -> if (checked) {
                    operator = "subtract"
                    Log.d("RADIO", "Subtract is checked")
                }
            }
        }
    }

    //Save instance through bundle
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("ANSWER", opResult)
        Log.i("LIFECYCLE", "saveInstanceState: $opResult")
    }

    //alternative way to state, MUST comment line 67-69 ///if (savedInstanceState != null) {...}/// to use this
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        opResult = savedInstanceState.getInt("ANSWER") //this is just the KEY to get the result
//        val answer = findViewById<TextView>(R.id.answer)
//        answer.text = opResult.toString()
//        Log.i("LIFECYCLE", "restoreInstanceState: $opResult")
//    }

    /*return Int type and convert number1 and number2 to int*/
    private fun add(number1: String, number2: String): Int = number1.toInt() + number2.toInt()
    private fun subtract(number1: String, number2: String): Int = number1.toInt() - number2.toInt()
}