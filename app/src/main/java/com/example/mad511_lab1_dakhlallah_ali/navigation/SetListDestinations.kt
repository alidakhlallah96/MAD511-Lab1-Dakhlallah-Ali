package com.example.mad511_lab1_dakhlallah_ali.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


sealed interface SetListDestination : NavKey {

    @Serializable
    data object ArtistList : SetListDestination

    @Serializable
    data object AddArtist : SetListDestination

    @Serializable
    data class ArtistDetail(val artistId: Int) : SetListDestination

    @Serializable
    data class ConfirmDelete(val artistId: Int) : SetListDestination
}