package com.example.mad511_lab1_dakhlallah_ali.di

import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.data.local.RoomArtistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Tells Hilt to bind the concrete RoomArtistRepository to the ArtistRepository interface
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindArtistRepository(
        repository: RoomArtistRepository
    ): ArtistRepository
}