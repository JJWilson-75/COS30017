package com.example.assignment3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Password::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "password_database"
                )
                    .addCallback(PasswordDatabaseCallback(scope)) // Add this only if needed, may delete later
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Callback to pre-populate the database
    private class PasswordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.passwordDao())
                }
            }
        }

        // Method to insert initial data
        private suspend fun populateDatabase(passwordDao: PasswordDao) {
            // Insert 10 sample password entries
            passwordDao.insert(Password(itemName = "Google", username = "john.doe92", password = "G00gle#2023!", websiteUrl = "https://google.com", note = "Main email account"))
            passwordDao.insert(Password(itemName = "Facebook", username = "jane_doe_88", password = "F@ceB00k$88", websiteUrl = "https://facebook.com", note = "Social media account"))
            passwordDao.insert(Password(itemName = "Amazon", username = "doe.john.amazon", password = "Am@z0n_789!", websiteUrl = "https://amazon.com", note = "Shopping account"))
            passwordDao.insert(Password(itemName = "Twitter", username = "johnny_doe22", password = "Tw!tt3r#2022", websiteUrl = "https://twitter.com", note = "Microblogging account"))
            passwordDao.insert(Password(itemName = "LinkedIn", username = "jane.doe.prof", password = "L!nk3dIn@2023", websiteUrl = "https://linkedin.com", note = "Professional network"))
            passwordDao.insert(Password(itemName = "GitHub", username = "johndoe_dev", password = "G!tHub#303$", websiteUrl = "https://github.com", note = "Code repository"))
            passwordDao.insert(Password(itemName = "Netflix", username = "doe_family", password = "N3tfl!x_404#", websiteUrl = "https://netflix.com", note = "Streaming service"))
            passwordDao.insert(Password(itemName = "Spotify", username = "jane_musiclover", password = "Sp0t!fy$505", websiteUrl = "https://spotify.com", note = "Music streaming"))
            passwordDao.insert(Password(itemName = "PayPal", username = "john.doe.payment", password = "P@yP@l_606!", websiteUrl = "https://paypal.com", note = "Payment account"))
            passwordDao.insert(Password(itemName = "Dropbox", username = "doe.storage", password = "Dr0pB0x#707$", websiteUrl = "https://dropbox.com", note = "Cloud storage"))
        }
    }
}