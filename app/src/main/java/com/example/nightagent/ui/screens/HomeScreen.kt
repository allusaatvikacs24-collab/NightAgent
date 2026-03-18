package com.example.nightagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightagent.ui.components.*
import com.example.nightagent.ui.theme.*

@Composable
fun HomeScreen(
    onSOSClick: () -> Unit,
    onFakeCallClick: () -> Unit,
    onSafeWalkClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        item {
            // Gradient Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PurpleStart, PurpleEnd)
                        ),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Safety First,",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 16.sp
                        )
                        Text(
                            "Agent Status",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    // Status Indicator
                    Surface(
                        color = SuccessGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(SuccessGreen, RoundedCornerShape(50))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "All Systems Active",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        item {
            // SOS Button Section
            SOSButton(onLongPress = onSOSClick)
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "Quick Actions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    QuickActionCard(
                        title = "Share Location",
                        icon = Icons.Default.LocationOn,
                        iconColor = InfoBlue,
                        onClick = {}
                    )
                    QuickActionCard(
                        title = "Fake Call",
                        icon = Icons.Default.Call,
                        iconColor = Color(0xFFFF9800),
                        onClick = onFakeCallClick
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    QuickActionCard(
                        title = "Safe Walk",
                        icon = Icons.Default.DirectionsWalk,
                        iconColor = SuccessGreen,
                        onClick = onSafeWalkClick
                    )
                    QuickActionCard(
                        title = "Record Evidence",
                        icon = Icons.Default.Mic,
                        iconColor = Lavender,
                        onClick = {}
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Recent Activity",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    TextButton(onClick = {}) {
                        Text("See All", color = PurpleEnd)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            ActivityItem(
                title = "Safe Walk Completed",
                time = "Today, 10:30 PM",
                icon = Icons.Default.CheckCircle,
                iconColor = SuccessGreen
            )
            ActivityItem(
                title = "Emergency Contacts Updated",
                time = "Yesterday, 06:15 PM",
                icon = Icons.Default.Person,
                iconColor = InfoBlue
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}