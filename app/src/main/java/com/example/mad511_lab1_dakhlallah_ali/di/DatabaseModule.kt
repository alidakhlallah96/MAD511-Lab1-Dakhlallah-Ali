package com.example.mad511_lab1_dakhlallah_ali.di

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.mad511_lab1_dakhlallah_ali.data.local.AppDatabase
import com.example.mad511_lab1_dakhlallah_ali.data.local.ArtistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

// Tells Hilt how to create database dependencies that live for the whole app session
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Keeps a single instance of the database open so connections don't overlap
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder<AppDatabase>(context, "artists.db")
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()

    // Provides the DAO from the database instance
    @Provides
    fun provideArtistDao(db: AppDatabase): ArtistDao = db.artistDao()
}