package com.example.mad511_lab1_dakhlallah_ali.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

//  custom Chicago Bears palette
private val BearsColorScheme = lightColorScheme(
    primary = BearsNavy,
    onPrimary = BearsWhite,
    secondary = BearsOrange,
    onSecondary = BearsWhite,
    background = BearsLightBackground,
    surface = BearsWhite
)

@Composable
fun ChicagoBearsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BearsColorScheme,
        typography = Typography,
        content = content
    )
}