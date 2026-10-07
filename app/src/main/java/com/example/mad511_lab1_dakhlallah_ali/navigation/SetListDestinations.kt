package com.example.mad511_lab1_dakhlallah_ali.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// sealed interface extending NavKey
@Serializable
sealed interface SetListDestination : NavKey {

    // start destination
    @Serializable
    data object ArtistList : SetListDestination

    // add artist screen
    @Serializable
    data object AddArtist : SetListDestination

    // artist detail screen
    @Serializable
    data class ArtistDetail(val artistId: Int) : SetListDestination

    // confirm delete screen
    @Serializable
    data class ConfirmDelete(val artistId: Int) : SetListDestination
}