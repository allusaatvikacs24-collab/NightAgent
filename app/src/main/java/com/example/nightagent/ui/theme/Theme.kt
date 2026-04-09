package com.example.nightagent.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.nightagent.sos.SafetySettings

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

private val DarkColorScheme = darkColorScheme(
    primary = Lavender,
    onPrimary = Color.White,
    secondary = BlushPink,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun NightagentTheme(content: @Composable () -> Unit) {

    val colorScheme =
        if (SafetySettings.darkMode.value) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}