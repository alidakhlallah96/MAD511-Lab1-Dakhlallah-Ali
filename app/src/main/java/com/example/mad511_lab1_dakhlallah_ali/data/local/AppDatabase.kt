package com.example.mad511_lab1_dakhlallah_ali.data.local

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

// Room database holding the artists table
@Database(entities = [ArtistEntity::class], version = 1, exportSchema = true)
@ColumnTypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao

    // Ensures only one database instance exists
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context.applicationContext).also { INSTANCE = it }
            }

        // Builds database using Room 3 bundled SQLite driver and IO dispatcher
        private fun build(context: Context) =
            Room.databaseBuilder<AppDatabase>(context, "artists.db")
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
    }
}