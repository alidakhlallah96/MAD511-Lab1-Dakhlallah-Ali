package com.example.mad511_lab1_dakhlallah_ali.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.AddArtistUiState
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.AddArtistViewModel

// Stateful Route composable
@Composable
fun AddArtistRoute(
    repository: ArtistRepository,
    onBackClick: () -> Unit
) {
    val viewModel: AddArtistViewModel = viewModel(
        factory = AddArtistViewModel.provideFactory(repository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddArtistScreen(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onGenreChange = viewModel::onGenreChange,
        onYearChange = viewModel::onYearChange,
        onSaveClick = { viewModel.saveArtist(onSuccess = onBackClick) },
        onBackClick = onBackClick
    )
}

// Stateless Screen composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArtistScreen(
    uiState: AddArtistUiState,
    onNameChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Artist") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text("Artist Name") },
                isError = uiState.isFormTouched && uiState.nameError != null,
                supportingText = {
                    if (uiState.isFormTouched && uiState.nameError != null) {
                        Text(uiState.nameError)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.genre,
                onValueChange = onGenreChange,
                label = { Text("Genre") },
                isError = uiState.isFormTouched && uiState.genreError != null,
                supportingText = {
                    if (uiState.isFormTouched && uiState.genreError != null) {
                        Text(uiState.genreError)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.yearFormed,
                onValueChange = onYearChange,
                label = { Text("Year Formed") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.isFormTouched && uiState.yearError != null,
                supportingText = {
                    if (uiState.isFormTouched && uiState.yearError != null) {
                        Text(uiState.yearError)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSaveClick,
                enabled = uiState.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Artist")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddArtistScreenPreview() {
    ChicagoBearsTheme {
        AddArtistScreen(
            uiState = AddArtistUiState(name = "Radiohead", genre = "Alternative", yearFormed = "1985"),
            onNameChange = {},
            onGenreChange = {},
            onYearChange = {},
            onSaveClick = {},
            onBackClick = {}
        )
    }
}