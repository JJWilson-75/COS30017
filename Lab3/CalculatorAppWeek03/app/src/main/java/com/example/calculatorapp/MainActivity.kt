package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    var operator = "plus"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

//        val plus = findViewById<RadioButton>(R.id.plus)
//        val subtract = findViewById<RadioButton>(R.id.subtract)
//        val operationRadioGroup = findViewById<RadioGroup>(R.id.operationRadioGroup)

//        plus.setOnCheckedChangeListener { buttonView, isChecked ->
//            Log.d("RADIO", "Plus is checked: $isChecked")
//            operator = "plus"
//        }
//
//        subtract.setOnCheckedChangeListener { buttonView, isChecked ->
//            Log.d("RADIO", "Subtract is checked: $isChecked")
//            operator = "subtract"
//        }

        equals.setOnClickListener{_ ->
            val result = when(operator)
            {
                "plus" -> add(number1.text.toString(), number2.text.toString())
                "subtract" -> subtract(number1.text.toString(), number2.text.toString())
                else -> add(number1.text.toString(), number2.text.toString())
            }

            answer.text = result.toString()
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

    /*return Int type and convert number1 and number2 to int*/
    private fun add(number1: String, number2: String): Int = number1.toInt() + number2.toInt()
    private fun subtract(number1: String, number2: String): Int = number1.toInt() - number2.toInt()
}