package com.example.womensafetyapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.womensafetyapp.ui.components.*

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Emergency SOS")

            Spacer(modifier = Modifier.height(10.dp))

            SOSButton { }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Quick Actions")

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(300.dp)
            ) {

                item { QuickActionCard("Share Location") }
                item { QuickActionCard("Fake Call") }
                item { QuickActionCard("Safe Walk") }
                item { QuickActionCard("Record Evidence") }

            }

            Text("Recent Activity")

            ActivityItem("Safe Walk Completed", "2 hours ago")
            ActivityItem("Location Shared", "5 hours ago")

        }

        BottomNavBar("home", onNavigate)

    }
}