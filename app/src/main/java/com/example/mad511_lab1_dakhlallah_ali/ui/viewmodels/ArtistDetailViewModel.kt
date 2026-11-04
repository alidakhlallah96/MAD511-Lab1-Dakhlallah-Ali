package com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state for detail screen
sealed interface ArtistDetailUiState {
    data class Success(val artist: Artist) : ArtistDetailUiState
    data object DeletedOrNotFound : ArtistDetailUiState
}

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val repository: ArtistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Retrieve artistId passed from navigation destination route
    private val artistId: Int = checkNotNull(savedStateHandle["artistId"])

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

    // Toggles favorite status for the current artist
    fun toggleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(artistId)
        }
    }

    // Deletes the current artist and triggers success callback
    fun deleteArtist(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteArtist(artistId)
            onSuccess()
        }
    }
}