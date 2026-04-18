package com.example.nightagent.sos

import androidx.compose.runtime.mutableStateOf

object SafetySettings {

    // Safety Screen
    val voiceSOS = mutableStateOf(false)
    val shakeDetection = mutableStateOf(false)
    val powerButtonSOS = mutableStateOf(false)
    val silentMode = mutableStateOf(false)
    val autoRecording = mutableStateOf(false)

    // Settings Screen
    val pushNotifications = mutableStateOf(true)
    val darkMode = mutableStateOf(false)
    val locationSharing = mutableStateOf(true)
    val autoRecordingConsent = mutableStateOf(true)
}