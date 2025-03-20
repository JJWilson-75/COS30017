package com.example.instrumentrentalapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.widget.TextView
import android.widget.RatingBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var chipGroupType: ChipGroup
    private lateinit var instrumentName: TextView
    private lateinit var instrumentType: TextView
    private lateinit var instrumentPrice: TextView
    private lateinit var instrumentStockNumber: TextView
    private lateinit var instrumentWeight: TextView
    private lateinit var instrumentPicture: ImageView
    private lateinit var instrumentRating: RatingBar
    private lateinit var btnLanguageToggle: ImageView
    private lateinit var btnNext: Button
    private lateinit var btnPrev: Button
    private lateinit var btnDetails: Button

    private var isEnglish = true // default language is English

    private lateinit var creditAccount : TextView
    private var currentCreditAccountBalance = 4000.0f //set default money can use in app to $4000

    private var instrumentsList = listOf(
        Instrument("Fender American Professional II Stratocaster", InstrumentType.GUITAR, 1500.25f, 5, 3.6f, 3.5f),
        Instrument("Gibson Les Paul Standard '50s", InstrumentType.GUITAR, 2500.5f, 3, 4.1f, 2.5f),
        Instrument("Steinway & Sons Model D Concert Grand", InstrumentType.PIANO, 170.5f, 2, 480.0f, 5.0f),
        Instrument("Yamaha FG800 Acoustic", InstrumentType.GUITAR, 220.75f, 7, 2.9f, 4.5f),
        Instrument("Yamaha P-125 Digital", InstrumentType.PIANO, 650.0f, 4, 110.8f, 2.5f),
        Instrument("Selmer Paris Series II Tenor", InstrumentType.SAXOPHONE, 3900.5f, 3, 3.2f, 4f)
    )

    private var filteredInstruments: List<Instrument> = instrumentsList
    private var currentInstrumentIndex = 0

    private val rentalHistory = mutableListOf<RentalInfo>() //Store many instances of equipment rental attempt

    private lateinit var rentalActivityLauncher: ActivityResultLauncher<Intent>

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

        //MUST restore instance state first before setContentView to force device to change language preference
        savedInstanceState?.let {
            isEnglish = it.getBoolean("LANGUAGE") // Default to English if null
            currentCreditAccountBalance = it.getFloat("currentCreditAccountBalance", 4000.0f)
        }

        //set language to either English or Vietnamese
        setLocale(if (isEnglish) "en_US" else "vi")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        creditAccount = findViewById(R.id.creditAccount)
        btnLanguageToggle = findViewById(R.id.btnChangeLang)
        chipGroupType = findViewById(R.id.chipGroupType)
        instrumentName = findViewById(R.id.instrumentName)
        instrumentType = findViewById(R.id.instrumentType)
        instrumentPrice = findViewById(R.id.instrumentPrice)
        instrumentStockNumber = findViewById(R.id.instrumentStockNumber)
        instrumentWeight = findViewById(R.id.instrumentWeight)
        instrumentPicture = findViewById(R.id.instrumentPicture)
        instrumentRating = findViewById(R.id.instrumentRating)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnDetails = findViewById(R.id.btnInstrumentBorrow)

        //Split the work saveInstanceState because can't restore checked status of guitar chip before setContentView
        savedInstanceState?.let {
            val guitarChecked = it.getBoolean("guitarChip")
            val pianoChecked = it.getBoolean("pianoChip")
            val saxophoneChecked = it.getBoolean("saxophoneChip")

            // Restore the chip selections
            findViewById<Chip>(R.id.chipGuitar).isChecked = guitarChecked
            findViewById<Chip>(R.id.chipPiano).isChecked = pianoChecked
            findViewById<Chip>(R.id.chipSaxophone).isChecked = saxophoneChecked

            // Restore the current instrument index
            currentInstrumentIndex = it.getInt("currentInstrumentIndex")

            val gson = Gson()
            val type = object : TypeToken<List<Instrument>>() {}.type
            val instrumentsListJson = it.getString("instrumentsListJson")
            instrumentsList = instrumentsListJson?.let { json -> gson.fromJson(json, type) } ?: instrumentsList

            val filteredInstrumentsJson = it.getString("filteredInstrumentsJson")
            filteredInstruments = filteredInstrumentsJson?.let { json -> gson.fromJson(json, type) } ?: instrumentsList

//            filterInstruments() //can't because it will reset currentInstrumentIndex
            displayInstrument(currentInstrumentIndex)

            //Necessary, can't delete this part
            val rentalHistoryJson = savedInstanceState.getString("rentalHistoryJson")
            if (!rentalHistoryJson.isNullOrEmpty()) {
                val type = object : TypeToken<List<RentalInfo>>() {}.type
                rentalHistory.clear()
                rentalHistory.addAll(Gson().fromJson(rentalHistoryJson, type))
                Log.i("BUNDLE", "restoreInstanceState: rentalHistory - $rentalHistoryJson")
            }
        }

        updateButtonChangeLanguageIcon()

        btnLanguageToggle.setOnClickListener {
            isEnglish = !isEnglish
            setLocale(if (isEnglish) "en_US" else "vi")
            recreate() // Restart activity to apply changes
        }

        chipGroupType.setOnCheckedStateChangeListener { _, _ ->
            filterInstruments()
        }

        btnNext.setOnClickListener {
            navigateInstrument(1)
            Log.i("NAVIGATION", "NEXT - Instrument: ${filteredInstruments[currentInstrumentIndex].name}")
        }

        btnPrev.setOnClickListener {
            navigateInstrument(-1)
            Log.i("NAVIGATION", "PREVIOUS - Instrument: ${filteredInstruments[currentInstrumentIndex].name}")
        }

        findViewById<Button>(R.id.btnBorrow).setOnClickListener {
            if (filteredInstruments[currentInstrumentIndex].stockNumber < 1) {
                Toast.makeText(this, getString(R.string.out_of_instrument_message), Toast.LENGTH_SHORT).show()
                //Make sure the user can't rent if there isn't any instruments of that type anymore
                return@setOnClickListener
            }

            Log.i("BORROW", "Instrument: ${filteredInstruments[currentInstrumentIndex].name}")
            Log.i("BORROW", "Language - English: $isEnglish")
            Log.i("BORROW", "Credit Account: $currentCreditAccountBalance")
            val intent = Intent(this, BookingActivity::class.java).apply {
                putExtra("LANGUAGE", isEnglish)
                putExtra("selectedInstrument", filteredInstruments[currentInstrumentIndex])
                putExtra("currentCreditAccountBalance", currentCreditAccountBalance)
            }

            rentalActivityLauncher.launch(intent)
        }

        btnDetails.setOnClickListener {
            showRentalHistoryDialog()
        }

        displayInstrument(currentInstrumentIndex)

        updateBtnDetailsState()

        rentalActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val selectedInstrument = it.getParcelableExtra<Instrument>("selectedInstrument") as Instrument
                    val rentalEmail = it.getStringExtra("rentalEmail") ?: ""
                    val rentalNumber = it.getIntExtra("rentalNumber", 0)
                    val rentalTime = it.getIntExtra("rentalTime", 0)
                    val rentalPrice = it.getFloatExtra("rentalPrice", 0.0f)

                    //Should delete this part if it mess too much with other parts of the code
                    val languagePreferenceFromRent = it.getBooleanExtra("isEnglish", isEnglish)
                    if (languagePreferenceFromRent != isEnglish) {
                        isEnglish = languagePreferenceFromRent
                        setLocale(if (isEnglish) "en_US" else "vi")
                        recreate() // Restart activity to apply language changes
                    }

                    currentCreditAccountBalance = it.getFloatExtra("currentCreditAccountBalance", currentCreditAccountBalance)
                    creditAccount.text = "$currentCreditAccountBalance"

                    // Add to rental history list
                    val rentalAttempt = RentalInfo(selectedInstrument, rentalEmail, rentalNumber, rentalTime, rentalPrice)
                    rentalHistory.add(rentalAttempt)
                    updateBtnDetailsState()
                    Log.i("RENTAL_HISTORY", "New rental attempt: ${selectedInstrument.name}")

                    instrumentsList.find { it.name == filteredInstruments[currentInstrumentIndex].name }?.let {
                        instrument -> instrument.stockNumber -= rentalNumber
                        filteredInstruments[currentInstrumentIndex].stockNumber = instrument.stockNumber //change the stock number of displayed instruments as well
                        Log.i("Reduce", "New stock number for default ${instrument.name}: ${instrument.stockNumber}")
                        Log.i("Reduce", "New stock number for filtered ${filteredInstruments[currentInstrumentIndex].name}: ${filteredInstruments[currentInstrumentIndex].stockNumber}")
                    } //Reduce the number of instruments in stock accordingly
                    displayInstrument(currentInstrumentIndex)

                    Toast.makeText(this, getString(R.string.rental_confirmation_message), Toast.LENGTH_SHORT).show()
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                result.data?.let {
                    val languagePreferenceFromRent = it.getBooleanExtra("isEnglish", isEnglish)
                    if (languagePreferenceFromRent != isEnglish) {
                        isEnglish = languagePreferenceFromRent
                        setLocale(if (isEnglish) "en_US" else "vi")
                        recreate() // Restart activity to apply language changes
                    }

                    Toast.makeText(this, getString(R.string.cancel_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Save instance through bundle
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("LANGUAGE", isEnglish) //save language preference in case of screen orientation
        Log.i("BUNDLE", "saveInstanceState: languageEnglish - $isEnglish")

        outState.putBoolean("guitarChip", findViewById<Chip>(R.id.chipGuitar).isChecked)
        outState.putBoolean("pianoChip", findViewById<Chip>(R.id.chipPiano).isChecked)
        outState.putBoolean("saxophoneChip", findViewById<Chip>(R.id.chipSaxophone).isChecked)
        Log.i("BUNDLE", "saveInstanceState: guitarChip - ${findViewById<Chip>(R.id.chipGuitar).isChecked}; pianoChip - ${findViewById<Chip>(R.id.chipPiano).isChecked}; saxophoneChip - ${findViewById<Chip>(R.id.chipSaxophone).isChecked}")

        outState.putInt("currentInstrumentIndex", currentInstrumentIndex)
        Log.i("BUNDLE", "saveInstanceState: currentInstrumentIndex - $currentInstrumentIndex")

        // Convert the filteredInstruments list to a JSON string
        val gson = Gson()
        val instrumentsJson = gson.toJson(filteredInstruments)
        outState.putString("filteredInstrumentsJson", instrumentsJson)
        Log.i("BUNDLE", "saveInstanceState: instrumentsJson - $instrumentsJson")

        val instrumentsListJson = gson.toJson(instrumentsList)
        outState.putString("instrumentsListJson", instrumentsListJson)
        Log.i("BUNDLE", "saveInstanceState: instrumentsListJson - $instrumentsListJson")

        outState.putFloat("currentCreditAccountBalance", currentCreditAccountBalance)
        Log.i("BUNDLE", "saveInstanceState: currentCreditAccountBalance - $currentCreditAccountBalance")

        val rentalHistoryJson = Gson().toJson(rentalHistory)
        outState.putString("rentalHistoryJson", rentalHistoryJson)
        Log.i("BUNDLE", "saveInstanceState: rentalHistory - $rentalHistoryJson")
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

    //Return English or Vietnamese version of instrument type based on language preference
    private fun getLocalizedType(type: InstrumentType): String {
        return when (type) {
            InstrumentType.GUITAR -> getString(R.string.guitar)
            InstrumentType.PIANO -> getString(R.string.piano)
            InstrumentType.SAXOPHONE -> getString(R.string.saxophone)
        }
    }

    private fun filterInstruments() {
        val selectedTypes = mutableSetOf<InstrumentType>()

        if (findViewById<Chip>(R.id.chipGuitar).isChecked) selectedTypes.add(InstrumentType.GUITAR)
        if (findViewById<Chip>(R.id.chipPiano).isChecked) selectedTypes.add(InstrumentType.PIANO)
        if (findViewById<Chip>(R.id.chipSaxophone).isChecked) selectedTypes.add(InstrumentType.SAXOPHONE)

        filteredInstruments = if (selectedTypes.isEmpty()) {
            instrumentsList // Show all if nothing is selected
        } else {
            instrumentsList.filter { it.type in selectedTypes }
        }

        currentInstrumentIndex = 0
        displayInstrument(currentInstrumentIndex)
    }

    private fun displayInstrument(index: Int) {
        val instrument = filteredInstruments[index]
        instrumentName.text = instrument.name
        instrumentType.text = getString(R.string.label_text_type) + ": " + getLocalizedType(instrument.type)
        instrumentPrice.text = getString(R.string.label_text_price) + ": " + "$${instrument.price}"
        instrumentStockNumber.text = getString(R.string.label_text_stock_number) + ": " + "${instrument.stockNumber}"
        instrumentWeight.text = getString(R.string.label_text_weight) + ": ${instrument.weight}kg"
        instrumentRating.rating = instrument.rating

        // Set instrument image according to instrument name
        instrumentPicture.setImageResource(when (instrument.name) {
            "Fender American Professional II Stratocaster" -> R.drawable.guitar_picture_1
            "Gibson Les Paul Standard '50s" -> R.drawable.guitar_picture_2
            "Steinway & Sons Model D Concert Grand" -> R.drawable.piano_picture_1
            "Yamaha FG800 Acoustic" -> R.drawable.guitar_picture_3
            "Yamaha P-125 Digital" -> R.drawable.piano_picture_2
            else -> R.drawable.saxophone_picture_1
        })

        creditAccount.text = "$currentCreditAccountBalance"

        // Disable buttons if only one instrument is available
        if (filteredInstruments.size == 1) {
            btnNext.isEnabled = false
            btnPrev.isEnabled = false
        } else {
            btnNext.isEnabled = true
            btnPrev.isEnabled = true
        }
    }

    private fun navigateInstrument(direction: Int) {
        if (filteredInstruments.isNotEmpty()) {
            currentInstrumentIndex += direction

            if (currentInstrumentIndex < 0) {
                currentInstrumentIndex = filteredInstruments.size - 1 //Roll back to largest index when hit previous
            } else if (currentInstrumentIndex >= filteredInstruments.size) {
                currentInstrumentIndex = 0  //Roll back to smallest index when hit next
            }

            displayInstrument(currentInstrumentIndex)
        }
    }

    //Enable or disable btnDetails based on if rentalHistory has any elements or not
    private fun updateBtnDetailsState() {
        btnDetails.isEnabled = rentalHistory.isNotEmpty()
    }

    private fun showRentalHistoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.label_btn_instrument_borrow))

        if (rentalHistory.isNotEmpty()) {
            val historyDetails = rentalHistory.mapIndexed { index, rental ->
                "+ ${getString(R.string.label_rental_id)}: ${index + 1}\n" +  // Add the index (starting from 1)
                        "+ ${getString(R.string.label_text_name)}: ${rental.instrument.name}\n" +
                        "+ ${getString(R.string.label_rental_email)} ${rental.email}\n" +
                        "+ ${getString(R.string.label_rental_number)}: ${rental.number}\n" +
                        "+ ${getString(R.string.label_rental_time)}: ${rental.time}\n" +
                        "+ ${getString(R.string.label_rental_price)}: $${rental.price}"
            }.joinToString("\n\n")

            builder.setMessage(historyDetails)
        }

        builder.setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}