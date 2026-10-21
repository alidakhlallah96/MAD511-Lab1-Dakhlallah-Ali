package com.example.mad511_lab1_dakhlallah_ali.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad511_lab1_dakhlallah_ali.Artist
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.ArtistListUiState
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.ArtistListViewModel

@Composable
fun ArtistListRoute(
    repository: ArtistRepository,
    onArtistClick: (Int) -> Unit,
    onAddArtistClick: () -> Unit
) {
    val viewModel: ArtistListViewModel = viewModel(
        factory = ArtistListViewModel.provideFactory(repository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArtistListScreen(
        query = uiState.query,
        onQueryChange = viewModel::onQueryChange,
        artists = uiState.artists,
        onArtistClick = onArtistClick,
        onAddArtistClick = onAddArtistClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListScreen(
    query: String,
    onQueryChange: (String) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Search artists") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (artists.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (query.isNotBlank()) "No artists match" else "No artists available")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(artists, key = { it.id }) { artist ->
                        ListItem(
                            headlineContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(artist.name)
                                    if (artist.isFavorite) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Favorite",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            },
                            supportingContent = { Text("${artist.genre} • Formed ${artist.yearFormed}") },
                            modifier = Modifier.clickable { onArtistClick(artist.id) }
                        )
                        HorizontalDivider()
                    }
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
            query = "",
            onQueryChange = {},
            artists = listOf(
                Artist(1, "The Beatles", "Rock", 1960, isFavorite = true),
                Artist(2, "Daft Punk", "Electronic", 1993, isFavorite = false)
            ),
            onArtistClick = {},
            onAddArtistClick = {}
        )
    }
}