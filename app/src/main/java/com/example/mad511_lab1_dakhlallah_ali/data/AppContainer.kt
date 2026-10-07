package com.example.mad511_lab1_dakhlallah_ali.data

// container class holding app dependencies
interface AppContainer {
    val artistRepository: ArtistRepository
}

// default that instantiates the fake repository
class DefaultAppContainer : AppContainer {
    override val artistRepository: ArtistRepository by lazy {
        FakeArtistRepository()
    }
}