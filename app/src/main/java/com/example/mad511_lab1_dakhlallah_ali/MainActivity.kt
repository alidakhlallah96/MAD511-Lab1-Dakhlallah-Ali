package com.example.mad511_lab1_dakhlallah_ali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    }
}