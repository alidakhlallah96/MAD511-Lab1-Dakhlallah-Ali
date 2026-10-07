package com.example.mad511_lab1_dakhlallah_ali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.mad511_lab1_dakhlallah_ali.data.FakeArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.navigation.SetListNavHost
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChicagoBearsTheme {
                val repository = remember { FakeArtistRepository() }
                SetListNavHost(repository = repository)
            }
        }
    }
}