package com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ArtistListViewModel(
    private val repository: ArtistRepository
) : ViewModel() {

    // Converts the repository flow into a state flow for the UI to observe
    val artists: StateFlow<List<Artist>> = repository.artists
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    // Manual factory so we can pass the repository into the constructor
    companion object {
        fun provideFactory(repository: ArtistRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArtistListViewModel(repository) as T
                }
            }
    }
}