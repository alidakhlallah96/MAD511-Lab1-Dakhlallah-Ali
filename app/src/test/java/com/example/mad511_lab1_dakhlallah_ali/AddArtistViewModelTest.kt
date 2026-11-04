package com.example.mad511_lab1_dakhlallah_ali

import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.AddArtistViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Fake repository implementation matching your actual ArtistRepository interface
class TestFakeArtistRepository : ArtistRepository {
    override val artists: Flow<List<Artist>> = flowOf(emptyList())

    override fun getArtist(id: Int): Flow<Artist?> = flowOf(null)

    override fun searchArtists(term: String): Flow<List<Artist>> = flowOf(emptyList())

    override suspend fun addArtist(name: String, genre: String, yearFormed: Int) {}

    override suspend fun toggleFavorite(id: Int) {}

    override suspend fun deleteArtist(id: Int) {}
}

class AddArtistViewModelTest {

    @Test
    fun yearValidation_rulesEnforcedCorrectly() {
        val fakeRepo = TestFakeArtistRepository()
        val viewModel = AddArtistViewModel(fakeRepo)

        // Set valid name and genre to isolate year validation
        viewModel.onNameChange("The Beatles")
        viewModel.onGenreChange("Rock")

        // Invalid year checks (< 1800 or > 2026)
        viewModel.onYearChange("1799")
        assertFalse(viewModel.uiState.value.isValid)

        viewModel.onYearChange("2027")
        assertFalse(viewModel.uiState.value.isValid)

        // Valid boundary year checks
        viewModel.onYearChange("1800")
        assertTrue(viewModel.uiState.value.isValid)

        viewModel.onYearChange("2026")
        assertTrue(viewModel.uiState.value.isValid)
    }
}