package com.example.instrumentrentalapp

import android.os.Parcelable
import android.os.Parcel

enum class InstrumentType(val key: String) {
    GUITAR("guitar"),
    PIANO("piano"),
    SAXOPHONE("saxophone");
}

data class Instrument(
    val name: String,
    val type: InstrumentType,
    val price: Float,
    var stockNumber: Int, //var to enable it being reduced when borrowed
    val weight: Float, // Weight in kg
    var rating: Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",  // For name string, if null return empty string
        InstrumentType.valueOf(parcel.readString() ?: InstrumentType.GUITAR.name),    // For type string, if null return "guitar" string
        parcel.readFloat(),  //?: 1.0f,  // For price float, if null return 1.0f
        parcel.readInt(),  //?: 0,  // Read stockNumber integer, if null return 0
        parcel.readFloat(), // Read weight
        parcel.readFloat(),  //?: 1.0f    // Read rating float, if null returns 1.0f
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(type.name)
        parcel.writeFloat(price)
        parcel.writeInt(stockNumber)
        parcel.writeFloat(weight)
        parcel.writeFloat(rating)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Instrument> {
        //This method reads the data from the Parcel and returns a new Instrument object.
        override fun createFromParcel(parcel: Parcel): Instrument {
            return Instrument(parcel)
        }

        //This is used when an array of Instrument objects needs to be created
        override fun newArray(size: Int): Array<Instrument?> {
            return arrayOfNulls(size)
        }
    }
}