package com.example.mad511_lab1_dakhlallah_ali.data.local

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.mad511_lab1_dakhlallah_ali.Artist
import java.time.Instant

// Room entity class representing the database table
@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "genre")
    val genre: String,

    @ColumnInfo(name = "year_formed")
    val yearFormed: Int,

    @ColumnInfo(name = "added_at")
    val addedAt: Instant = Instant.now()
)

// Maps entity to domain model
fun ArtistEntity.toArtist(): Artist {
    return Artist(
        id = id,
        name = name,
        genre = genre,
        yearFormed = yearFormed,
        addedAt = addedAt
    )
}

// Maps domain model to entity
fun Artist.toEntity(): ArtistEntity {
    return ArtistEntity(
        id = id,
        name = name,
        genre = genre,
        yearFormed = yearFormed,
        addedAt = addedAt
    )
}