package com.example.nightagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.nightagent.ui.components.ToggleRow
import com.example.nightagent.ui.theme.*
import com.example.nightagent.sos.SafetySettings

@Composable
fun SettingsScreen() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {

            Text(
                text = "Notifications & Sounds",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            ToggleRow(
                title = "Push Notifications",
                subtitle = "Receive important safety alerts",
                icon = Icons.Default.Notifications,
                checked = SafetySettings.pushNotifications.value
            ) {
                SafetySettings.pushNotifications.value = it
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {

            Text(
                text = "Appearance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            ToggleRow(
                title = "Dark Mode",
                subtitle = "Switch to dark theme",
                icon = Icons.Default.DarkMode,
                checked = SafetySettings.darkMode.value
            ) {
                SafetySettings.darkMode.value = it
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {

            Text(
                text = "Privacy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            ToggleRow(
                title = "Location Sharing",
                subtitle = "Share location with contacts",
                icon = Icons.Default.LocationOn,
                checked = SafetySettings.locationSharing.value
            ) {
                SafetySettings.locationSharing.value = it
            }

            ToggleRow(
                title = "Auto Recording Consent",
                subtitle = "Record audio on SOS",
                icon = Icons.Default.Videocam,
                checked = SafetySettings.autoRecordingConsent.value
            ) {
                SafetySettings.autoRecordingConsent.value = it
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}