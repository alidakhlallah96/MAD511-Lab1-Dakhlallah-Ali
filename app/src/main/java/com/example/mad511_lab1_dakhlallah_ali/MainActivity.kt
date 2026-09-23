package com.example.mad511_lab1_dakhlallah_ali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Just for commit
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SetListApp()
            }
        }
    }
}

@Composable
fun SetListApp() {
    // List state to keep track of added artists
    val artistList = remember { mutableStateListOf<Artist>() }

    // Text field state variables
    var nameInput by rememberSaveable { mutableStateOf("") }
    var genreInput by rememberSaveable { mutableStateOf("") }
    var yearInput by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Name input field
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Artist: ") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Genre input field
        OutlinedTextField(
            value = genreInput,
            onValueChange = { genreInput = it },
            label = { Text("Genre: ") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Year input field
        OutlinedTextField(
            value = yearInput,
            onValueChange = { yearInput = it },
            label = { Text("Year Formed: ") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
      //
        Spacer(modifier = Modifier.height(16.dp))

        // Add button logic
        Button(
            onClick = {
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Artist")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn for dynamic list with delete action passed in
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(artistList) { artist ->
                ArtistRow(
                    artist = artist,
                    onDelete = { artistList.remove(artist) }
                )
            }
        }
    }
}

// Single row layout with delete buttom and logic
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