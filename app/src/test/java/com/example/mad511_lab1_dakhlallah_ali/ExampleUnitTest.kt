package com.example.mad511_lab1_dakhlallah_ali

import com.example.mad511_lab1_dakhlallah_ali.data.FakeArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.AddArtistViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun yearValidation_outOfRangeYearDisablesAddButton_sensibleYearEnablesIt() {
        val repository = FakeArtistRepository()
        val viewModel = AddArtistViewModel(repository)

        // Fill required fields with valid inputs
        viewModel.onNameChange("The Beatles")
        viewModel.onGenreChange("Rock")

        //  Out-of-range year keeps Add button disabled
        viewModel.onYearChange("1700")
        assertFalse(
            "Add button should be disabled for an out-of-range year",
            viewModel.uiState.value.isValid
        )

        //  valid year enables Add button
        viewModel.onYearChange("1960")
        assertTrue(
            "Add button should be enabled when year and all fields are valid",
            viewModel.uiState.value.isValid
        )
    }
}