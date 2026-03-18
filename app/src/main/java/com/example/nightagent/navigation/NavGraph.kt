package com.example.nightagent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nightagent.ui.screens.*
import com.example.nightagent.ui.components.BottomNavBar

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "splash"

    val fullScreenRoutes = listOf("splash", "fakecall", "safewalk", "sosactive")
    val showBottomBar = currentRoute !in fullScreenRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    selected = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(if (showBottomBar) innerPadding else PaddingValues())
        ) {
            composable("splash") { 
                SplashScreen(onTimeout = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }) 
            }
            composable("home") { 
                HomeScreen(
                    onSOSClick = { navController.navigate("sosactive") },
                    onFakeCallClick = { navController.navigate("fakecall") },
                    onSafeWalkClick = { navController.navigate("safewalk") }
                ) 
            }
            composable("map") { MapScreen() }
            composable("contacts") { ContactsScreen() }
            composable("safety") { SafetyScreen() }
            composable("settings") { SettingsScreen() }
            composable("fakecall") { FakeCallScreen(onDismiss = { navController.popBackStack() }) }
            composable("safewalk") { SafeWalkScreen() }
            composable("sosactive") { SOSActivatedScreen() }
        }
    }
}
