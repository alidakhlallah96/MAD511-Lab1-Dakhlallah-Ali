package com.example.mad511_lab1_dakhlallah_ali.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme
import com.example.mad511_lab1_dakhlallah_ali.ui.viewmodels.ArtistDetailViewModel

// Stateful Route composable
@Composable
fun ConfirmDeleteRoute(
    artistId: Int,
    repository: ArtistRepository,
    onDeleted: () -> Unit,
    onDismiss: () -> Unit
) {
    val viewModel: ArtistDetailViewModel = viewModel(
        factory = ArtistDetailViewModel.provideFactory(artistId, repository)
    )

    ConfirmDeleteScreen(
        onConfirm = { viewModel.deleteArtist(onSuccess = onDeleted) },
        onDismiss = onDismiss
    )
}

// Stateless Screen composable
@Composable
fun ConfirmDeleteScreen(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Artist") },
        text = { Text("Are you sure you want to delete this artist?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Delete") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmDeleteScreenPreview() {
    ChicagoBearsTheme {
        ConfirmDeleteScreen(onConfirm = {}, onDismiss = {})
    }
}