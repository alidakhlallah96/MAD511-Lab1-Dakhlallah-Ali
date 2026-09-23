package com.example.mad511_lab1_dakhlallah_ali

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme

// Stateful caller
@Composable
fun SetListScreen() {
    // List state to keep track of added artists
    val artistList = remember { mutableStateListOf<Artist>() }

    // Text field state variables
    var nameInput by rememberSaveable { mutableStateOf("") }
    var genreInput by rememberSaveable { mutableStateOf("") }
    var yearInput by rememberSaveable { mutableStateOf("") }

    // Check if year is non number when typed
    val yearError = yearInput.isNotEmpty() && yearInput.toIntOrNull() == null

    // Wrap screen in Scaffold
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            SetListContent(
                nameInput = nameInput,
                onNameChange = { nameInput = it },
                genreInput = genreInput,
                onGenreChange = { genreInput = it },
                yearInput = yearInput,
                onYearChange = { yearInput = it },
                yearError = yearError,
                artistList = artistList,
                onAddArtist = {
                    val parsedYear: Int = yearInput.toIntOrNull() ?: 0

                    val newArtist = Artist(
                        name = nameInput.trim(),
                        genre = genreInput.trim(),
                        yearFormed = parsedYear
                    )

                    artistList.add(newArtist)

                    // Clear inputs after adding
                    nameInput = ""
                    genreInput = ""
                    yearInput = ""
                },
                onDeleteArtist = { artist ->
                    artistList.remove(artist)
                }
            )
        }
    }
}

// Stateless child composable
@Composable
fun SetListContent(
    nameInput: String,
    onNameChange: (String) -> Unit,
    genreInput: String,
    onGenreChange: (String) -> Unit,
    yearInput: String,
    onYearChange: (String) -> Unit,
    yearError: Boolean = false, // Accept error state parameter
    artistList: List<Artist>,
    onAddArtist: () -> Unit,
    onDeleteArtist: (Artist) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name input field
        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChange,
            label = { Text("Artist") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Genre input field
        OutlinedTextField(
            value = genreInput,
            onValueChange = onGenreChange,
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Year field with error handling and reserved text space
        OutlinedTextField(
            value = yearInput,
            onValueChange = onYearChange,
            label = { Text("Year Formed") },
            isError = yearError,
            supportingText = {
                if (yearError) {
                    Text("Year must be a valid number")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Add button
        Button(
            onClick = onAddArtist,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Artist")
        }

        // LazyColumn for dynamic list with delete action passed in
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(artistList) { artist ->
                ArtistRow(
                    artist = artist,
                    onDelete = { onDeleteArtist(artist) }
                )
            }
        }
    }
}

// Single row layout with delete button and logic
@Composable
fun ArtistRow(
    artist: Artist,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = artist.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Genre: ${artist.genre}")
            Text(text = "Formed: ${artist.yearFormed}")
        }

        Button(onClick = onDelete) {
            Text("Delete")
        }
    }
}

// Preview for stateless form and sample data with my theme
@Preview(showBackground = true)
@Composable
fun SetListContentPreview() {
    ChicagoBearsTheme {
        SetListContent(
            nameInput = "Drake",
            onNameChange = {},
            genreInput = "Rap",
            onGenreChange = {},
            yearInput = "2008",
            onYearChange = {},
            yearError = false, // Passed false for default preview state
            artistList = listOf(
                Artist("The Weekend", "RnB", 2010),
                Artist("Drake", "Rap", 2008)
            ),
            onAddArtist = {},
            onDeleteArtist = {}
        )
    }
}