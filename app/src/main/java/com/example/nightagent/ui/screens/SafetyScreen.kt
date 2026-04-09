package com.example.nightagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightagent.ui.components.ToggleRow
import com.example.nightagent.ui.theme.*
import com.example.nightagent.sos.SafetySettings

@Composable
fun SafetyScreen() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Safety Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Shield,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(
                            "Safe Environment",
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen,
                            fontSize = 18.sp
                        )

                        Text(
                            "No threats detected nearby",
                            color = SuccessGreen.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {

            Text(
                "Automation Features",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {

            ToggleRow(
                title = "Voice SOS",
                subtitle = "Trigger SOS via keyword",
                icon = Icons.Default.Mic,
                checked = SafetySettings.voiceSOS.value
            ) { SafetySettings.voiceSOS.value = it }

            ToggleRow(
                title = "Shake Detection",
                subtitle = "SOS on vigorous shake",
                icon = Icons.Default.Smartphone,
                checked = SafetySettings.shakeDetection.value
            ) { SafetySettings.shakeDetection.value = it }

            ToggleRow(
                title = "Power Button",
                subtitle = "3-press emergency trigger",
                icon = Icons.Default.PowerSettingsNew,
                checked = SafetySettings.powerButtonSOS.value
            ) { SafetySettings.powerButtonSOS.value = it }

            ToggleRow(
                title = "Silent Mode",
                subtitle = "Stealth alert without noise",
                icon = Icons.Default.VolumeOff,
                checked = SafetySettings.silentMode.value
            ) { SafetySettings.silentMode.value = it }

            ToggleRow(
                title = "Auto Recording",
                subtitle = "Start audio recording on SOS",
                icon = Icons.Default.Videocam,
                checked = SafetySettings.autoRecording.value
            ) { SafetySettings.autoRecording.value = it }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}