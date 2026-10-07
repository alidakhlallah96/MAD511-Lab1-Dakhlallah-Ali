package com.example.mad511_lab1_dakhlallah_ali.data

import com.example.mad511_lab1_dakhlallah_ali.Artist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

// Interface describing what the app needs from artist data
interface ArtistRepository {
    // Flow of all artists that the UI can observe
    val artists: Flow<List<Artist>>

    // look up a single artist by id
    fun getArtist(id: Int): Flow<Artist?>

    // Suspend function to add an artist
    suspend fun addArtist(name: String, genre: String, yearFormed: Int)

    // Suspend function to delete an artist by id
    suspend fun deleteArtist(id: Int)
}

class FakeArtistRepository : ArtistRepository {

    // Initial sample data so list isn't empty on launch
    private val initialArtists = listOf(
        Artist(id = 1, name = "The Weeknd", genre = "RnB", yearFormed = 2010),
        Artist(id = 2, name = "Drake", genre = "Rap", yearFormed = 2008)
    )

    // Private state flow for internal updates
    private val _artists = MutableStateFlow(initialArtists)

    override val artists: Flow<List<Artist>> = _artists.asStateFlow()

    // Auto-incrementing ID
    private var nextId = 3

    // artist updates matching the target ID
    override fun getArtist(id: Int): Flow<Artist?> {
        return _artists.map { list -> list.firstOrNull { it.id == id } }
    }

    // Assigns nextId and appends artist atomically
    override suspend fun addArtist(name: String, genre: String, yearFormed: Int) {
        _artists.update { currentList ->
            val newArtist = Artist(
                id = nextId++,
                name = name,
                genre = genre,
                yearFormed = yearFormed
            )
            currentList + newArtist
        }
    }

    // Removes artist matching given ID
    override suspend fun deleteArtist(id: Int) {
        _artists.update { currentList ->
            currentList.filterNot { it.id == id }
        }
    }
}