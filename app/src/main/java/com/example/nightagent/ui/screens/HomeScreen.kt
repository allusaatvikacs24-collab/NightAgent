package com.example.nightagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightagent.ui.components.*
import com.example.nightagent.ui.theme.*

@Composable
fun HomeScreen() {

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(White)
    ) {

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(PurpleGradientStart, PurpleGradientEnd)
                        )
                    )
                    .padding(20.dp)
            ) {

                Column {
                    Text("Welcome back,", color = White)
                    Text(
                        "Agent",
                        fontSize = 28.sp,
                        color = White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.2f))
                    ) {
                        Text(
                            "All Systems Active",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = White
                        )
                    }
                }
            }
        }

        item {
            SOSButton()
        }

        item {
            Text(
                "Quick Actions",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                color = BlackText
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                QuickActionCard("Share Location")
                QuickActionCard("Fake Call")

            }
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                QuickActionCard("Safe Walk")
                QuickActionCard("Record Evidence")

            }

        }

        item {
            Text(
                "Recent Activity",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                color = BlackText
            )
        }

        item {
            ActivityItem("Safe Walk Completed", "2 hours ago")
            ActivityItem("Location Shared", "5 hours ago")
        }

    }
}