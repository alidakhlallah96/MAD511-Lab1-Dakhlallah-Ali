package com.example.mad511_lab1_dakhlallah_ali.data

import com.example.mad511_lab1_dakhlallah_ali.Artist
import kotlinx.serialization.Serializable

// single artist entry for the JSON backup
@Serializable
data class ArtistBackupItem(
    val name: String,
    val genre: String,
    val yearFormed: Int,
    val isFavorite: Boolean,
)

// full JSON backup file with timestamp and list of artists
@Serializable
data class SetListBackup(
    val createdAt: Long,
    val artists: List<ArtistBackupItem>,
)

// convert Artist to its backup model
fun Artist.toBackupItem() = ArtistBackupItem(
    name = name,
    genre = genre,
    yearFormed = yearFormed,
    isFavorite = isFavorite,
)