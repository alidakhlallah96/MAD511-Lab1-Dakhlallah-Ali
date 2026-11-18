package com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.work.BackupScheduler
import com.example.mad511_lab1_dakhlallah_ali.work.BackupStatus
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

// Single immutable UiState wrapping search query, artists, and backup status
data class ArtistListUiState(
    val query: String = "",
    val artists: List<Artist> = emptyList(),
    val backupStatus: BackupStatus = BackupStatus.Idle
)

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    private val repository: ArtistRepository,
    private val backupScheduler: BackupScheduler
) : ViewModel() {

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

    // Watch the backup status flow from the scheduler
    private val backupStatus: StateFlow<BackupStatus> = backupScheduler.status()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BackupStatus.Idle)

    // Combined UiState flow exposed to the screen
    val uiState: StateFlow<ArtistListUiState> = combine(_query, results, backupStatus) { query, artists, status ->
        ArtistListUiState(query = query, artists = artists, backupStatus = status)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ArtistListUiState())

    // Triggers a manual backup through the scheduler
    fun backupNow() {
        backupScheduler.backupNow()
    }
}