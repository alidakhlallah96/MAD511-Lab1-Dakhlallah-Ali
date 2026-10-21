package com.example.mad511_lab1_dakhlallah_ali

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mad511_lab1_dakhlallah_ali.data.local.AppDatabase
import com.example.mad511_lab1_dakhlallah_ali.data.local.ArtistDao
import com.example.mad511_lab1_dakhlallah_ali.data.local.ArtistEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ArtistDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = db.artistDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun verifyDaoOperations() = runBlocking {
        // Insert several initial artists
        val artistA = ArtistEntity(id = 1, name = "The Weeknd", genre = "RnB", yearFormed = 2010)
        val artistB = ArtistEntity(id = 2, name = "Drake", genre = "Rap", yearFormed = 2008)
        val artistC = ArtistEntity(id = 3, name = "Adele", genre = "Pop", yearFormed = 2006)

        dao.upsertArtist(artistA)
        dao.upsertArtist(artistB)
        dao.upsertArtist(artistC)

        // 1. Reading all artists returns them ordered by name ("Adele", "Drake", "The Weeknd")
        val allArtists = dao.getAllArtists().first()
        assertEquals(3, allArtists.size)
        assertEquals("Adele", allArtists[0].name)
        assertEquals("Drake", allArtists[1].name)
        assertEquals("The Weeknd", allArtists[2].name)

        // 2. Prefix search returns only matching artists regardless of case
        val searchResults = dao.searchArtists("dr").first()
        assertEquals(1, searchResults.size)
        assertEquals("Drake", searchResults[0].name)

        // 3.  artist with an existing ID updates it rather than adding a second row
        val updatedDrake = ArtistEntity(id = 2, name = "Drake Updated", genre = "Hip Hop", yearFormed = 2008)
        dao.upsertArtist(updatedDrake)

        val artistsAfterUpsert = dao.getAllArtists().first()
        assertEquals(3, artistsAfterUpsert.size)

        val fetchedUpdatedArtist = dao.getArtist(2).first()
        assertEquals("Drake Updated", fetchedUpdatedArtist?.name)
        assertEquals("Hip Hop", fetchedUpdatedArtist?.genre)
    }
}