package com.example.mad511_lab1_dakhlallah_ali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mad511_lab1_dakhlallah_ali.ui.theme.ChicagoBearsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChicagoBearsTheme {
                SetListScreen()
            }
        }
    }
}