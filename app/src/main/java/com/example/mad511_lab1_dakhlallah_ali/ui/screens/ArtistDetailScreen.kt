package com.example.mad511_lab1_dakhlallah_ali.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.ArtistDetailUiState
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.ArtistDetailViewModel

// Stateful Route composable
@Composable
fun ArtistDetailRoute(
    artistId: Int,
    repository: ArtistRepository,
    onConfirmDeleteClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: ArtistDetailViewModel = viewModel(
        factory = ArtistDetailViewModel.provideFactory(artistId, repository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArtistDetailScreen(
        uiState = uiState,
        onConfirmDeleteClick = { onConfirmDeleteClick(artistId) },
        onBackClick = onBackClick
    )
}

// Stateless Screen composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    uiState: ArtistDetailUiState,
    onConfirmDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artist Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is ArtistDetailUiState.Success) {
                        IconButton(onClick = onConfirmDeleteClick) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Artist")
                        }
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (uiState) {
                is ArtistDetailUiState.Success -> {
                    Text(text = "Name: ${uiState.artist.name}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Genre: ${uiState.artist.genre}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Year Formed: ${uiState.artist.yearFormed}", style = MaterialTheme.typography.bodyLarge)
                }
                is ArtistDetailUiState.DeletedOrNotFound -> {
                    Text(text = "Artist not found or deleted.", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistDetailScreenPreview() {
    ChicagoBearsTheme {
        ArtistDetailScreen(
            uiState = ArtistDetailUiState.Success(Artist(1, "Pink Floyd", "Progressive Rock", 1965)),
            onConfirmDeleteClick = {},
            onBackClick = {}
        )
    }
}