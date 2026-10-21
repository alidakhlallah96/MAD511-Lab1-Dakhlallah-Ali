package com.example.mad511_lab1_dakhlallah_ali.data

import android.content.Context
import com.example.mad511_lab1_dakhlallah_ali.data.local.AppDatabase
import com.example.mad511_lab1_dakhlallah_ali.data.local.RoomArtistRepository

// Container class holding app dependencies
interface AppContainer {
    val artistRepository: ArtistRepository
}

// Default container initializing the Room-backed repository
class DefaultAppContainer(context: Context) : AppContainer {
    override val artistRepository: ArtistRepository by lazy {
        RoomArtistRepository(AppDatabase.get(context).artistDao())
    }
}