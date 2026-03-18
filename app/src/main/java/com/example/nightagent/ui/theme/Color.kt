package com.example.nightagent.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val PurpleStart = Color(0xFF9C27B0)
val PurpleEnd = Color(0xFFE91E63)
val PinkAccent = Color(0xFFFF4081)
val SOSRed = Color(0xFFAA336A)
val SOSGlow = Color(0x40AA336A)

val BackgroundLight = Color(0xFFFBF8FC)
val SurfaceLight = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF2D2D2D)
val TextSecondary = Color(0xFF757575)

val SuccessGreen = Color(0xFF4CAF50)
val InfoBlue = Color(0xFF2196F3)

val BlushPink = Color(0xFFF8BBD9)
val Lavender = Color(0xFFE1BEE7)
val LightPurple = Color(0xFFCE93D8)
val PastelBg = Color(0xFFFAF9FC)
val SoftShadow = Color(0x0F000000)

val CardShadow = Color(0x1A000000)

// Backward compatibility
val PinkPrimary = PurpleEnd
val PinkLight = BlushPink
val PurpleGradientStart = Lavender
val PurpleGradientEnd = PurpleEnd
val White = SurfaceLight
val BlackText = TextPrimary

val PinkPurpleGradient = Brush.verticalGradient(
    0f to BlushPink, 
    0.5f to Lavender, 
    1f to PurpleEnd
)
val PurplePinkGradient = Brush.horizontalGradient(
    0f to Lavender, 
    1f to BlushPink
)
