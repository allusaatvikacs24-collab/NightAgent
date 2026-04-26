package com.example.nightagent.navigation
import com.example.nightagent.ui.screens.   MapScreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import com.example.nightagent.sos.SOSManager
import androidx.compose.foundation.layout.*
import com.example.nightagent.ui.screens.*
import com.example.nightagent.ui.components.BottomNavBar
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NavGraph(onShareLocationClick: () -> Unit) {

    val navController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "splash"
    val coroutineScope = rememberCoroutineScope()

    val fullScreenRoutes = listOf("splash", "fakecall", "sosactive", "safewalk")
    val showBottomBar = currentRoute !in fullScreenRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    selected = currentRoute,
                    onNavigate = { route ->
                        coroutineScope.launch {
                            navController.navigate(route) {
                                popUpTo("home") {   // ✅ FIXED
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
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
                    onSOSClick = {
                        SOSManager.triggerSOS(context) {
                            navController.navigate("home") {
                                popUpTo("home")   // ✅ FIXED
                            }
                        }
                    },
                    onFakeCallClick = {
                        context.startActivity(android.content.Intent(context, com.example.nightagent.ui.activities.FakeCallActivity::class.java))
                    },
                    onSafeWalkClick = { navController.navigate("safewalk") },
                    onShareLocationClick = onShareLocationClick
                )
            }

            composable("safewalk") {
                SafeWalkScreen()
            }

            // ✅ REAL SCREENS (NO MORE PLACEHOLDERS)
            composable("map") {
                MapScreen()
            }

            composable("contacts") {
                ContactsScreen()
            }

            composable("safety") {
                SafetyScreen(onFakeCallClick = { context.startActivity(android.content.Intent(context, com.example.nightagent.ui.activities.FakeCallActivity::class.java)) })
            }

            composable("settings") {
                SettingsScreen()
            }

            composable("sosactive") {
                SOSActivatedScreen()
            }
        }
    }
}

@Composable
fun SOSActivatedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.padding(32.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "SOS ACTIVATED",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("SMS sent, alarm active", fontSize = 16.sp)
            }
        }
    }
}