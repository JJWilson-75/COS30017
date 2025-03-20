package com.example.instrumentrentalapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import android.content.res.Configuration
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.slider.Slider
import android.widget.Button
import android.widget.Toast

class BookingActivity : AppCompatActivity() {

    private lateinit var btnLanguageToggle: ImageView
    private lateinit var creditAccount : TextView
    private lateinit var rentalEmail : EditText
    private lateinit var btnConfirm : Button
    private lateinit var btnCancel : Button
    private lateinit var labelRentalNumber: TextView
    private lateinit var labelRentalTime: TextView
    private lateinit var rentalPrice: TextView
    private lateinit var rentalNumberSlider: Slider
    private lateinit var rentalTimeSlider: Slider

    private var isEnglish = true // default language is English
    private var selectedInstrument: Instrument? = null
    private var currentCreditAccountBalance = 0.0f

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

        // Restore language preference before setting content view
        savedInstanceState?.let {
            isEnglish = it.getBoolean("LANGUAGE", true)
        } ?: run {
            isEnglish = intent.getBooleanExtra("LANGUAGE", true)
        }

        // Set language to either English or Vietnamese
        setLocale(if (isEnglish) "en_US" else "vi")

        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)

        btnLanguageToggle = findViewById(R.id.btnChangeLang)
        creditAccount = findViewById(R.id.creditAccount)
        rentalNumberSlider = findViewById(R.id.rentalNumber)
        rentalTimeSlider = findViewById(R.id.rentalTime)
        labelRentalNumber = findViewById(R.id.labelRentalNumber)
        labelRentalTime = findViewById(R.id.labelRentalTime)
        rentalPrice = findViewById(R.id.rentalPrice)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnCancel = findViewById(R.id.btnCancel)
        rentalEmail = findViewById(R.id.rentalEmail)

        updateButtonChangeLanguageIcon()

        btnLanguageToggle.setOnClickListener {
            isEnglish = !isEnglish
            setLocale(if (isEnglish) "en_US" else "vi")
            recreate() // Restart activity to apply changes
        }

        // Retrieve the selected instrument
//        savedInstanceState?.let {
//            selectedInstrument = it.getParcelable("selectedInstrument")
//            currentCreditAccountBalance = it.getFloat("currentCreditAccountBalance", 0.0f)
//
//            // Restore rentalEmail, rentalTime, and rentalNumber
//            rentalEmail.setText(it.getString("rentalEmail", ""))
//            rentalTimeSlider.value = it.getFloat("rentalTime", 1f)
//            rentalNumberSlider.value = it.getFloat("rentalNumber", 1f)
//
//            labelRentalNumber.text = getString(R.string.label_rental_number) + ": " + rentalNumberSlider.value.toInt()
//            labelRentalTime.text = getString(R.string.label_rental_time) + ": " + rentalTimeSlider.value.toInt()
//            updateRentalPrice()
//        } ?: run {
            selectedInstrument = intent.getParcelableExtra("selectedInstrument")
            currentCreditAccountBalance = intent.getFloatExtra("currentCreditAccountBalance", 0.0f)
//        }

        selectedInstrument?.let { instrument ->
            findViewById<TextView>(R.id.instrumentName).text = instrument.name
            findViewById<ImageView>(R.id.instrumentPicture).setImageResource(getInstrumentImage(instrument.name))

            //Specically to deal with the case the stock number of instrument is 1
            if (instrument.stockNumber == 1) {
                rentalNumberSlider.visibility = View.GONE
                rentalNumberSlider.value = 1f
                labelRentalNumber.text = getString(R.string.label_rental_number) + ": 1"
            } else {
                rentalNumberSlider.visibility = View.VISIBLE
                rentalNumberSlider.valueTo = instrument.stockNumber.toFloat()
                rentalNumberSlider.value = 1f
            }
        }

        creditAccount.text = "$currentCreditAccountBalance"

        // **Set initial values for sliders and rental price**
        rentalNumberSlider.value = 1f
        rentalTimeSlider.value = 1f
        labelRentalNumber.text = getString(R.string.label_rental_number) + ": 1"
        labelRentalTime.text = getString(R.string.label_rental_time) + ": 1"
        updateRentalPrice() // Call function to calculate initial price

        btnConfirm.setOnClickListener {
            val email = rentalEmail.text.toString().trim()
            if (email == "") {
                Toast.makeText(this, getString(R.string.empty_email_message), Toast.LENGTH_SHORT).show()
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, getString(R.string.invalid_email_message), Toast.LENGTH_SHORT).show()
            } else if (calculateTotalPrice() > currentCreditAccountBalance) {
                Toast.makeText(this, getString(R.string.insufficient_balance_message), Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with booking logic
                currentCreditAccountBalance -= calculateTotalPrice()

                val resultIntent = Intent().apply {
                    putExtra("selectedInstrument", selectedInstrument)
                    putExtra("rentalEmail", email)
                    putExtra("rentalNumber", rentalNumberSlider.value.toInt())
                    putExtra("rentalTime", rentalTimeSlider.value.toInt())
                    putExtra("rentalPrice", calculateTotalPrice())
                    putExtra("currentCreditAccountBalance", currentCreditAccountBalance)

                    putExtra("isEnglish", isEnglish)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        btnCancel.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("isEnglish", isEnglish)
            }

            setResult(RESULT_CANCELED, resultIntent)
            finish()
        }

        rentalNumberSlider.addOnChangeListener { _, value, _ ->
            labelRentalNumber.text = getString(R.string.label_rental_number) + ": " + value.toInt()
            updateRentalPrice()
        }

        rentalTimeSlider.addOnChangeListener { _, value, _ ->
            labelRentalTime.text = getString(R.string.label_rental_time) + ": " + value.toInt()
            updateRentalPrice()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("LANGUAGE", isEnglish) // Save language preference
        Log.i("BUNDLE", "saveInstanceState: languageEnglish - $isEnglish")
//        outState.putParcelable("selectedInstrument", selectedInstrument)
//        Log.i("BUNDLE", "saveInstanceState: selectedInstrument - ${selectedInstrument?.name}")
//        outState.putFloat("currentCreditAccountBalance", currentCreditAccountBalance) // Save balance
//        Log.i("BUNDLE", "saveInstanceState: currentCreditAccountBalance - $currentCreditAccountBalance")
//
//        outState.putString("rentalEmail", rentalEmail.text.toString())
//        outState.putFloat("rentalTime", rentalTimeSlider.value)
//        outState.putFloat("rentalNumber", rentalNumberSlider.value)
//        Log.i("BUNDLE", "saveInstanceState: rentalEmail - ${rentalEmail.text}; rentalTime - ${rentalTimeSlider.value}; rentalNumber - ${rentalNumberSlider.value}")
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun updateButtonChangeLanguageIcon() {
        val iconRes = if (isEnglish) R.drawable.flag_us else R.drawable.flag_vietnam
        btnLanguageToggle.setImageResource(iconRes)
    }

    private fun getInstrumentImage(instrumentName: String): Int {
        return when (instrumentName) {
            "Fender American Professional II Stratocaster" -> R.drawable.guitar_picture_1
            "Gibson Les Paul Standard '50s" -> R.drawable.guitar_picture_2
            "Steinway & Sons Model D Concert Grand" -> R.drawable.piano_picture_1
            "Yamaha FG800 Acoustic" -> R.drawable.guitar_picture_3
            "Yamaha P-125 Digital" -> R.drawable.piano_picture_2
            else -> R.drawable.saxophone_picture_1
        }
    }

    private fun updateRentalPrice() {
        selectedInstrument?.let { instrument ->
            rentalPrice.text = getString(R.string.label_rental_price) + " $" + calculateTotalPrice()
        }
    }

    private fun calculateTotalPrice(): Float {
        selectedInstrument?.let { instrument ->
            val rentalTime = rentalTimeSlider.value.toInt()
            val rentalNumber = rentalNumberSlider.value.toInt()
            return instrument.price * rentalTime * rentalNumber
        }

        return 0f // Return 0 only if no instrument is selected
    }

    override fun onBackPressed() {
        // Trigger the Cancel button click logic
        btnCancel.performClick()

        super.onBackPressed()
    }
}