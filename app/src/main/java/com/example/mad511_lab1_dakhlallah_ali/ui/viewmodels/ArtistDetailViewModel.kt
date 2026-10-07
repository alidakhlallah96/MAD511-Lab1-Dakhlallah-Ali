package com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// UI state for detail screen
sealed interface ArtistDetailUiState {
    data class Success(val artist: Artist) : ArtistDetailUiState
    data object DeletedOrNotFound : ArtistDetailUiState
}

class ArtistDetailViewModel(
    private val artistId: Int,
    private val repository: ArtistRepository
) : ViewModel() {

    // Observes artist by ID from the repository
    val uiState: StateFlow<ArtistDetailUiState> = repository.getArtist(artistId)
        .map { artist ->
            if (artist != null) ArtistDetailUiState.Success(artist)
            else ArtistDetailUiState.DeletedOrNotFound
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ArtistDetailUiState.DeletedOrNotFound
        )

    // Deletes the current artist and triggers success callback
    fun deleteArtist(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteArtist(artistId)
            onSuccess()
        }
    }

    companion object {
        fun provideFactory(artistId: Int, repository: ArtistRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArtistDetailViewModel(artistId, repository) as T
                }
            }
    }
}