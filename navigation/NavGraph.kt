package com.example.womensafetyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.womensafetyapp.ui.screens.*

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {

        composable("home") {
            HomeScreen {
                navController.navigate(it)
            }
        }

        composable("map") { MapScreen() }
        composable("contacts") { ContactsScreen() }
        composable("safety") { SafetyScreen() }
        composable("settings") { SettingsScreen() }

    }
}