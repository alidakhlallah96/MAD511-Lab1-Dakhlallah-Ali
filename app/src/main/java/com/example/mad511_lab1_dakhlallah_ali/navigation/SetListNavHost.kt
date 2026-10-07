package com.example.mad511_lab1_dakhlallah_ali.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.AddArtistRoute
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.ArtistDetailRoute
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.ArtistListRoute
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.ConfirmDeleteRoute

@Composable
fun SetListNavHost(repository: ArtistRepository) {

    val backStack = rememberNavBackStack<SetListDestination>(SetListDestination.ArtistList)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = {
            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
        },
        popTransitionSpec = {
            slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
        },
        predictivePopTransitionSpec = {
            slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
        },
        entryProvider = entryProvider {

            entry<SetListDestination.ArtistList> {
                ArtistListRoute(
                    repository = repository,
                    onArtistClick = { id -> backStack.add(SetListDestination.ArtistDetail(id)) },
                    onAddArtistClick = { backStack.add(SetListDestination.AddArtist) }
                )
            }

            entry<SetListDestination.AddArtist> {
                AddArtistRoute(
                    repository = repository,
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }

            entry<SetListDestination.ArtistDetail> { key: SetListDestination.ArtistDetail ->
                ArtistDetailRoute(
                    artistId = key.artistId,
                    repository = repository,
                    onConfirmDeleteClick = { id -> backStack.add(SetListDestination.ConfirmDelete(id)) },
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }

            entry<SetListDestination.ConfirmDelete> { key: SetListDestination.ConfirmDelete ->
                ConfirmDeleteRoute(
                    artistId = key.artistId,
                    repository = repository,
                    onDeleted = {
                        backStack.removeLastOrNull()
                        backStack.removeLastOrNull()
                    },
                    onDismiss = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}