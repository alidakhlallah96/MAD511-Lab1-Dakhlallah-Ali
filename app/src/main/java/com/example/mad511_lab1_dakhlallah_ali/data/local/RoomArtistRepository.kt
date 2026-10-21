package com.example.mad511_lab1_dakhlallah_ali.data.local

import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

// Room implementation of ArtistRepository mapping
class RoomArtistRepository(private val dao: ArtistDao) : ArtistRepository {

    override val artists: Flow<List<Artist>> =
        dao.getAllArtists().map { rows -> rows.map { it.toArtist() } }

    override fun getArtist(id: Int): Flow<Artist?> =
        dao.getArtist(id).map { entity -> entity?.toArtist() }

    override fun searchArtists(term: String): Flow<List<Artist>> =
        dao.searchArtists(term).map { rows -> rows.map { it.toArtist() } }

    override suspend fun addArtist(name: String, genre: String, yearFormed: Int) {
        val entity = ArtistEntity(
            id = 0,
            name = name,
            genre = genre,
            yearFormed = yearFormed,
            addedAt = Instant.now()
        )
        dao.upsertArtist(entity)
    }

    override suspend fun deleteArtist(id: Int) = dao.deleteArtist(id)
}