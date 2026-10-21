package com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

// Single immutable UiState wrapping search query and matching artists
data class ArtistListUiState(
    val query: String = "",
    val artists: List<Artist> = emptyList()
)

class ArtistListViewModel(private val repository: ArtistRepository) : ViewModel() {

    // The text in the search box, held here so it survives rotation
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun onQueryChange(value: String) {
        _query.value = value
    }

    // Pipeline from typed query to database matches
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val results: StateFlow<List<Artist>> = _query
        .debounce(300) // Wait for typing to pause before querying
        .distinctUntilChanged() // Skip query if search text has not changed
        .flatMapLatest { term ->
            if (term.isBlank()) repository.artists else repository.searchArtists(term)
        } // New term cancels previous query
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Combined UiState flow exposed to the screen
    val uiState: StateFlow<ArtistListUiState> = combine(_query, results) { query, artists ->
        ArtistListUiState(query = query, artists = artists)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ArtistListUiState())

    companion object {
        fun provideFactory(repository: ArtistRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArtistListViewModel(repository) as T
                }
            }
    }
}