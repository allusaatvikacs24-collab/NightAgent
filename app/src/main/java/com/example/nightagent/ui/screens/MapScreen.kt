package com.example.nightagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightagent.ui.theme.*

@Composable
fun MapScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Mock Map Background
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Lavender.copy(alpha = 0.4f)
            )
            Text(
                "Map View Placeholder",
                color = TextSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Top Search Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            color = SurfaceLight,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, null, tint = TextSecondary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Search safe zones...", color = TextSecondary, fontSize = 16.sp)
            }
        }

        // Bottom Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "Nearby Safety Points",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                NearbyPlaceItem("Police Station", "0.5 km", Icons.Default.Security, Color(0xFF1976D2))
                NearbyPlaceItem("City Hospital", "1.2 km", Icons.Default.LocalHospital, Color(0xFFD32F2F))
                NearbyPlaceItem("Safe Zone Cafe", "2.0 km", Icons.Default.VerifiedUser, SuccessGreen)
            }
        }

        // Floating Action Buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 220.dp) // Adjusted to be above the bottom card
        ) {
            FloatingActionButton(
                onClick = {},
                containerColor = BlushPink,
                contentColor = PurpleEnd,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(Icons.Default.Directions, "Safe Route")
            }
            FloatingActionButton(
                onClick = {},
                containerColor = Lavender,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.MyLocation, "Locate Me")
            }
        }
    }
}

@Composable
fun NearbyPlaceItem(
    name: String,
    distance: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
            Text(distance, color = TextSecondary, fontSize = 14.sp)
        }
        IconButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = PurpleEnd)
        }
    }
}