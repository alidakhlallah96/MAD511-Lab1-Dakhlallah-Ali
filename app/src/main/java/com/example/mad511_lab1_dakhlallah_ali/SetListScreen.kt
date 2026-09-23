package com.example.mad511_lab1_dakhlallah_ali

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme

// Stateful caller
@Composable
fun SetListScreen() {
    // List state initialized with sample data so the app isn't blank on launch
    val artistList = remember {
        mutableStateListOf(
            Artist("The Weeknd", "RnB", 2010),
            Artist("Drake", "Rap", 2008)
        )
    }

    // Text field state variables
    var nameInput by rememberSaveable { mutableStateOf("") }
    var genreInput by rememberSaveable { mutableStateOf("") }
    var yearInput by rememberSaveable { mutableStateOf("") }

    // validation rules
    val nameError = nameInput.isNotEmpty() && nameInput.trim().isEmpty()
    val genreError = genreInput.isNotEmpty() && genreInput.trim().isEmpty()

    // year formed must be within this range
    val parsedYear = yearInput.toIntOrNull()
    val yearError = yearInput.isNotEmpty() && (parsedYear == null || parsedYear < 1900 || parsedYear > 2026)

    // Guard the Add button with derivedStateOf
    val isFormValid by remember {
        derivedStateOf {
            nameInput.trim().isNotEmpty() &&
                    genreInput.trim().isNotEmpty() &&
                    yearInput.trim().isNotEmpty() &&
                    !nameError && !genreError && !yearError
        }
    }

    // Wrap screen in Scaffold
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            SetListContent(
                nameInput = nameInput,
                onNameChange = { nameInput = it },
                nameError = nameError,
                genreInput = genreInput,
                onGenreChange = { genreInput = it },
                genreError = genreError,
                yearInput = yearInput,
                onYearChange = { yearInput = it },
                yearError = yearError,
                isAddEnabled = isFormValid,
                artistList = artistList,
                onAddArtist = {
                    val year: Int = yearInput.toIntOrNull() ?: 0

                    val newArtist = Artist(
                        name = nameInput.trim(),
                        genre = genreInput.trim(),
                        yearFormed = year
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
    nameError: Boolean = false,
    genreInput: String,
    onGenreChange: (String) -> Unit,
    genreError: Boolean = false,
    yearInput: String,
    onYearChange: (String) -> Unit,
    yearError: Boolean = false,
    isAddEnabled: Boolean = false,
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
        // Name input field with error handling
        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChange,
            label = { Text("Artist") },
            isError = nameError,
            supportingText = if (nameError) {
                { Text("Name cannot be empty") }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Genre input field with error handling
        OutlinedTextField(
            value = genreInput,
            onValueChange = onGenreChange,
            label = { Text("Genre") },
            isError = genreError,
            supportingText = if (genreError) {
                { Text("Genre cannot be empty") }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Year field with error handling, numbered keyboard, and range check
        OutlinedTextField(
            value = yearInput,
            onValueChange = onYearChange,
            label = { Text("Year Formed") },
            isError = yearError,
            supportingText = if (yearError) {
                { Text("Enter a valid year (1900 - 2026)") }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Add button
        Button(
            onClick = onAddArtist,
            enabled = isAddEnabled,
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
            nameError = false,
            genreInput = "Rap",
            onGenreChange = {},
            genreError = false,
            yearInput = "2008",
            onYearChange = {},
            yearError = false,
            artistList = listOf(
                Artist("The Weeknd", "RnB", 2010),
                Artist("Drake", "Rap", 2008)
            ),
            onAddArtist = {},
            onDeleteArtist = {}
        )
    }
}