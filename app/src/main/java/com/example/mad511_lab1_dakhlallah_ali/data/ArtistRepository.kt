package com.example.mad511_lab1_dakhlallah_ali.data

import com.example.mad511_lab1_dakhlallah_ali.Artist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.Instant

// Interface describing what the app needs from artist data
interface ArtistRepository {
    // Flow of all artists that the UI can observe
    val artists: Flow<List<Artist>>

    // look up a single artist by id
    fun getArtist(id: Int): Flow<Artist?>

    // Search artists whose name starts with search term
    fun searchArtists(term: String): Flow<List<Artist>>

    // Suspend function to add an artist
    suspend fun addArtist(name: String, genre: String, yearFormed: Int)

    // Suspend function to toggle favorite status
    suspend fun toggleFavorite(id: Int)

    // Suspend function to delete an artist by id
    suspend fun deleteArtist(id: Int)
}

class FakeArtistRepository : ArtistRepository {

    // Initial sample data so list isn't empty on launch
    private val initialArtists = listOf(
        Artist(id = 1, name = "The Weeknd", genre = "RnB", yearFormed = 2010, addedAt = Instant.now()),
        Artist(id = 2, name = "Drake", genre = "Rap", yearFormed = 2008, addedAt = Instant.now())
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

    // Search artists matching given prefix
    override fun searchArtists(term: String): Flow<List<Artist>> {
        return _artists.map { list -> list.filter { it.name.startsWith(term, ignoreCase = true) } }
    }

    // sets addedAt timestamp when creating a new artist
    override suspend fun addArtist(name: String, genre: String, yearFormed: Int) {
        _artists.update { currentList ->
            val newArtist = Artist(
                id = nextId++,
                name = name,
                genre = genre,
                yearFormed = yearFormed,
                addedAt = Instant.now()
            )
            currentList + newArtist
        }
    }

    // Toggles favorite status for artist matching given ID
    override suspend fun toggleFavorite(id: Int) {
        _artists.update { currentList ->
            currentList.map { artist ->
                if (artist.id == id) artist.copy(isFavorite = !artist.isFavorite) else artist
            }
        }
    }

    // Removes artist matching given ID
    override suspend fun deleteArtist(id: Int) {
        _artists.update { currentList ->
            currentList.filterNot { it.id == id }
        }
    }
}