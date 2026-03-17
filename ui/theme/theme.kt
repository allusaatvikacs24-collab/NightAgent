package com.example.nightagent.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun WomenSafetyTheme(content: @Composable () -> Unit) {

    val colorScheme = lightColorScheme(
        primary = PinkPrimary,
        background = White
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}