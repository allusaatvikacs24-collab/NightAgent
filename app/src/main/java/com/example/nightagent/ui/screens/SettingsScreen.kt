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

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                "Notifications & Sounds",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            ToggleRow("Push Notifications", "Receive important safety alerts", Icons.Default.Notifications) { }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                "Appearance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            ToggleRow("Dark Mode", "Switch to dark theme", Icons.Default.DarkMode) { }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                "Privacy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            ToggleRow("Location Sharing", "Share location with contacts", Icons.Default.LocationOn) { }
            ToggleRow("Auto Recording Consent", "Record video on SOS", Icons.Default.Videocam) { }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
