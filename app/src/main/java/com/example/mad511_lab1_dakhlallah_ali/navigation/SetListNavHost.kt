package com.example.mad511_lab1_dakhlallah_ali.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.navEntryDecorator
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.AddArtistRoute
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.ArtistDetailRoute
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.ArtistListRoute
import com.example.mad511_lab1_dakhlallah_ali.ui.screens.ConfirmDeleteRoute

@Composable
fun SetListNavHost() {

    val backStack = rememberNavBackStack<SetListDestination>(SetListDestination.ArtistList)

    val saveableStateHolder = rememberSaveableStateHolder()

    // 1. SaveableState Decorator
    val saveableStateDecorator = navEntryDecorator<SetListDestination>(
        onPop = { _ -> }
    ) { entry ->
        saveableStateHolder.SaveableStateProvider(key = entry.key.toString()) {
            entry.content(entry.key)
        }
    }

    // Capture the activity/parent owner to pass Hilt's default factory down
    val parentOwner = LocalViewModelStoreOwner.current

    // 2. ViewModelStore Decorator
    val viewModelStores = remember { mutableMapOf<String, ViewModelStore>() }

    val viewModelStoreDecorator = navEntryDecorator<SetListDestination>(
        onPop = { poppedKey ->
            (poppedKey as? SetListDestination)?.let { key ->
                viewModelStores.remove(key.toString())?.clear()
            }
        }
    ) { entry ->
        val keyString = entry.key.toString()
        val viewModelStore = viewModelStores.getOrPut(keyString) { ViewModelStore() }

        val viewModelStoreOwner = remember(keyString, parentOwner) {
            object : ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
                override val viewModelStore: ViewModelStore = viewModelStore

                override val defaultViewModelProviderFactory: ViewModelProvider.Factory
                    get() = (parentOwner as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
                        ?: ViewModelProvider.NewInstanceFactory()

                override val defaultViewModelCreationExtras: CreationExtras
                    get() = (parentOwner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras
                        ?: CreationExtras.Empty
            }
        }

        CompositionLocalProvider(
            LocalViewModelStoreOwner provides viewModelStoreOwner
        ) {
            entry.content(entry.key)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            saveableStateDecorator,
            viewModelStoreDecorator
        ),
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
                    onArtistClick = { id -> backStack.add(SetListDestination.ArtistDetail(id)) },
                    onAddArtistClick = { backStack.add(SetListDestination.AddArtist) }
                )
            }

            entry<SetListDestination.AddArtist> {
                AddArtistRoute(
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }

            entry<SetListDestination.ArtistDetail> { key: SetListDestination.ArtistDetail ->
                ArtistDetailRoute(
                    artistId = key.artistId,
                    onConfirmDeleteClick = { id -> backStack.add(SetListDestination.ConfirmDelete(id)) },
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }

            entry<SetListDestination.ConfirmDelete>(
                metadata = DialogSceneStrategy.dialog()
            ) { key: SetListDestination.ConfirmDelete ->
                ConfirmDeleteRoute(
                    artistId = key.artistId,
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