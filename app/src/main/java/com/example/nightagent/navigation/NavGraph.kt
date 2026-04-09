package com.example.nightagent.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.nightagent.sos.SOSManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*

import com.example.nightagent.ui.screens.*
import com.example.nightagent.ui.components.BottomNavBar

@Composable
fun NavGraph(
    onShareLocationClick: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
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
                    onSOSClick = {

                        SOSManager.triggerSOS(context)
                        navController.navigate("sosactive")
                    },
                    onFakeCallClick = { navController.navigate("fakecall") },
                    onSafeWalkClick = { navController.navigate("safewalk") },
                    onShareLocationClick = onShareLocationClick
                )
            }
            composable("safewalk") {
                SafeWalkScreen()
            }

            composable("map") { MapScreen() }
            composable("contacts") { ContactsScreen() }
            composable("safety") { SafetyScreen() }
            composable("settings") { SettingsScreen() }

            composable("fakecall") {
                FakeCallScreen(onDismiss = { navController.popBackStack() })
            }

            composable("safewalk") {
                SafeWalkScreen()
            }

            composable("sosactive") {
                SOSActivatedScreen()
            }
        }
    }
}

/* ---------------- TEMPORARY SCREENS ---------------- */

@Composable
fun FakeCallScreen(onDismiss: () -> Unit) {

    var isAnswered by remember { mutableStateOf(false) }
    var seconds by remember { mutableStateOf(0) }

    // Timer starts only after answering
    LaunchedEffect(isAnswered) {
        if (isAnswered) {
            while (true) {
                delay(1000)
                seconds++
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!isAnswered) {
                Text(
                    text = "Incoming Call",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Caller Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.DarkGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    fontSize = 50.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Mom",
                fontSize = 36.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isAnswered)
                    "Call Time 00:${seconds.toString().padStart(2, '0')}"
                else
                    "Calling...",
                color = Color.LightGray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                if (!isAnswered) {

                    // Decline button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color.Red, CircleShape)
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CallEnd,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Decline", color = Color.White)
                    }

                    // Answer button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color.Green, CircleShape)
                                .clickable { isAnswered = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Call,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Answer", color = Color.White)
                    }

                } else {

                    // End Call button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.Red, CircleShape)
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CallEnd,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("End Call", color = Color.White)
                    }
                }
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
        Text("SOS Activated")
    }
}