package com.example.mad511_lab1_dakhlallah_ali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mad511_lab1_dakhlallah_ali.navigation.SetListNavHost
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get the Room backed repository from application container
        val appContainer = (application as SetListApplication).container
        val repository = appContainer.artistRepository

        setContent {
            ChicagoBearsTheme {
                SetListNavHost(repository = repository)
            }
        }
    }
}