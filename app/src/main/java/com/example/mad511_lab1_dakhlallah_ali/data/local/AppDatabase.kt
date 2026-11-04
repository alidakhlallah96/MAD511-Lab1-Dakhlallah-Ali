package com.example.mad511_lab1_dakhlallah_ali.data.local

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

// Room database holding the artists table
@Database(entities = [ArtistEntity::class], version = 2, exportSchema = true)
@ColumnTypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artistDao(): ArtistDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override suspend fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE artists ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}