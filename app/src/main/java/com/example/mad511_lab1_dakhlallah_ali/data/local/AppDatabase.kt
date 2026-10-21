package com.example.mad511_lab1_dakhlallah_ali.data.local

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

// Room database holding the artists table
@Database(entities = [ArtistEntity::class], version = 2, exportSchema = true)
@ColumnTypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao

    // Ensures only one database instance exists
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override suspend fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE artists ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context.applicationContext).also { INSTANCE = it }
            }

        // Builds database using Room 3 bundled SQLite driver and IO dispatcher
        private fun build(context: Context) =
            Room.databaseBuilder<AppDatabase>(context, "artists.db")
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}