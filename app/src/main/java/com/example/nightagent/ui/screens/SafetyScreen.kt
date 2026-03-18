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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightagent.ui.components.ToggleRow
import com.example.nightagent.ui.theme.*

@Composable
fun SafetyScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelBg)
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Safety Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            // Safety Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.1f))
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
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            ToggleRow("Voice SOS", "Trigger SOS via keyword", Icons.Default.Mic) { }
            ToggleRow("Shake Detection", "SOS on vigorous shake", Icons.Default.Smartphone) { }
            ToggleRow("Power Button", "5-press emergency trigger", Icons.Default.PowerSettingsNew) { }
            ToggleRow("Silent Mode", "Stealth alert without noise", Icons.Default.VolumeOff) { }
            ToggleRow("Auto Recording", "Start camera on SOS", Icons.Default.Videocam) { }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
