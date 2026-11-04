package com.example.mad511_lab1_dakhlallah_ali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mad511_lab1_dakhlallah_ali.navigation.SetListNavHost
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme
import dagger.hilt.android.AndroidEntryPoint

// Allows Hilt to inject dependencies into ViewModels used inside this activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChicagoBearsTheme {
                SetListNavHost()
            }
        }
    }
}