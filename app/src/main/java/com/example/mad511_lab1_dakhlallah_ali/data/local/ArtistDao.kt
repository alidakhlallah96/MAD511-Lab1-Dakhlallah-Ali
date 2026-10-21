package com.example.mad511_lab1_dakhlallah_ali.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    // Gets all artists sorted alphabetically by name
    @Query("SELECT * FROM artists ORDER BY name ASC")
    fun getAllArtists(): Flow<List<ArtistEntity>>

    // Gets a single artist by ID, emitting null if not found
    @Query("SELECT * FROM artists WHERE id = :id")
    fun getArtist(id: Int): Flow<ArtistEntity?>

    // Prefix search uses the name index for fast lookups instead of scanning every row
    @Query("SELECT * FROM artists WHERE name LIKE :term || '%' ORDER BY name ASC")
    fun searchArtists(term: String): Flow<List<ArtistEntity>>

    // Inserts a new artist or updates an existing one
    @Upsert
    suspend fun upsertArtist(artist: ArtistEntity)

    // Deletes an artist from the database by ID
    @Query("DELETE FROM artists WHERE id = :id")
    suspend fun deleteArtist(id: Int)
}