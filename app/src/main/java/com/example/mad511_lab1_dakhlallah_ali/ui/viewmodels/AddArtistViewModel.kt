package com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Holds form state and error messages for adding an artist
data class AddArtistUiState(
    val name: String = "",
    val genre: String = "",
    val yearFormed: String = "",
    val nameError: String? = null,
    val genreError: String? = null,
    val yearError: String? = null,
    val isFormTouched: Boolean = false
) {
    // Only true when all fields are filled out correctly
    val isValid: Boolean
        get() = name.isNotBlank() &&
                genre.isNotBlank() &&
                yearFormed.toIntOrNull()?.let { it in 1800..2026 } == true &&
                nameError == null &&
                genreError == null &&
                yearError == null
}

class AddArtistViewModel(
    private val repository: ArtistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddArtistUiState())
    val uiState: StateFlow<AddArtistUiState> = _uiState.asStateFlow()

    // Updates name field and checks validation
    fun onNameChange(name: String) {
        val error = if (name.isBlank()) "Name cannot be empty" else null
        _uiState.value = _uiState.value.copy(name = name, nameError = error, isFormTouched = true)
    }

    // Updates genre field and checks validation
    fun onGenreChange(genre: String) {
        val error = if (genre.isBlank()) "Genre cannot be empty" else null
        _uiState.value = _uiState.value.copy(genre = genre, genreError = error, isFormTouched = true)
    }

    // Updates year field and validates numeric input
    fun onYearChange(year: String) {
        val parsedYear = year.toIntOrNull()
        val error = when {
            year.isBlank() -> "Year cannot be empty"
            parsedYear == null || parsedYear !in 1800..2026 -> "Enter a valid year (1800-2026)"
            else -> null
        }
        _uiState.value = _uiState.value.copy(yearFormed = year, yearError = error, isFormTouched = true)
    }

    // Saves valid artist data to repository and triggers navigation callback
    fun saveArtist(onSuccess: () -> Unit) {
        val state = _uiState.value
        val yearInt = state.yearFormed.toIntOrNull()
        if (state.isValid && yearInt != null) {
            viewModelScope.launch {
                repository.addArtist(state.name, state.genre, yearInt)
                onSuccess()
            }
        }
    }

    companion object {
        fun provideFactory(repository: ArtistRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AddArtistViewModel(repository) as T
                }
            }
    }
}