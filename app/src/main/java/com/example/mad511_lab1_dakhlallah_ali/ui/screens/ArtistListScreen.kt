package com.example.mad511_lab1_dakhlallah_ali.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.ArtistListViewModel

// Stateful Route composable
@Composable
fun ArtistListRoute(
    repository: ArtistRepository,
    onArtistClick: (Int) -> Unit,
    onAddArtistClick: () -> Unit
) {
    val viewModel: ArtistListViewModel = viewModel(
        factory = ArtistListViewModel.provideFactory(repository)
    )
    val artists by viewModel.artists.collectAsStateWithLifecycle()

    ArtistListScreen(
        artists = artists,
        onArtistClick = onArtistClick,
        onAddArtistClick = onAddArtistClick
    )
}

// Stateless Screen composable - handles UI only
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListScreen(
    artists: List<Artist>,
    onArtistClick: (Int) -> Unit,
    onAddArtistClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Artists") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddArtistClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Artist")
            }
        }
    ) { innerPadding ->
        if (artists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No artists available. Tap '+' to add one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    items = artists,
                    key = { artist -> artist.id }
                ) { artist ->
                    ListItem(
                        headlineContent = { Text(artist.name) },
                        supportingContent = { Text("${artist.genre} • Formed ${artist.yearFormed}") },
                        modifier = Modifier.clickable { onArtistClick(artist.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistListScreenPreview() {
    ChicagoBearsTheme {
        ArtistListScreen(
            artists = listOf(
                Artist(1, "The Beatles", "Rock", 1960),
                Artist(2, "Daft Punk", "Electronic", 1993)
            ),
            onArtistClick = {},
            onAddArtistClick = {}
        )
    }
}