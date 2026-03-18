package com.example.nightagent.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BlushPink,
    onPrimary = PurpleEnd,
    secondary = Lavender,
    onSecondary = PurpleStart,
    tertiary = LightPurple,
    background = PastelBg,
    surface = SurfaceLight,
    onSurfaceVariant = SurfaceLight.copy(alpha = 0.9f),
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun NightagentTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
