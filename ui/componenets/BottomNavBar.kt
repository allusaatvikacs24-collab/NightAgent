package com.example.womensafetyapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar(selected: String, onNavigate: (String) -> Unit) {

    NavigationBar {

        NavigationBarItem(
            selected = selected == "home",
            onClick = { onNavigate("home") },
            label = { Text("Home") },
            icon = {}
        )

        NavigationBarItem(
            selected = selected == "map",
            onClick = { onNavigate("map") },
            label = { Text("Map") },
            icon = {}
        )

        NavigationBarItem(
            selected = selected == "contacts",
            onClick = { onNavigate("contacts") },
            label = { Text("Contacts") },
            icon = {}
        )

        NavigationBarItem(
            selected = selected == "safety",
            onClick = { onNavigate("safety") },
            label = { Text("Safety") },
            icon = {}
        )

        NavigationBarItem(
            selected = selected == "settings",
            onClick = { onNavigate("settings") },
            label = { Text("Settings") },
            icon = {}
        )
    }
}