package com.example.nightagent.ui.screens

import org.osmdroid.views.overlay.Polyline
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

import com.example.nightagent.sos.LocationProvider
import com.example.nightagent.sos.SafeWalkManager

import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun SafeWalkScreen() {

    val context = LocalContext.current

    var isWalking by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }
    val walkPath = remember { mutableStateListOf<GeoPoint>() }

    var mapViewRef by remember { mutableStateOf<MapView?>(null) }

    LaunchedEffect(isWalking) {
        while (isWalking) {
            LocationProvider.getLocation(context) { location: Location? ->
                location?.let {
                    val point = GeoPoint(it.latitude, it.longitude)
                    userLocation = point
                    walkPath.add(point)
                }
            }
            kotlinx.coroutines.delay(3000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (isWalking) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    Configuration.getInstance().setUserAgentValue(ctx.packageName)

                    val mapView = MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(17.0)
                    }

                    mapViewRef = mapView
                    mapView
                },
                update = { mapView ->

                    userLocation?.let { location ->

                        mapView.controller.setCenter(location)
                        mapView.overlays.clear()

                        if (walkPath.size > 1) {
                            val polyline = Polyline()
                            polyline.setPoints(walkPath)
                            polyline.outlinePaint.color = android.graphics.Color.RED
                            polyline.outlinePaint.strokeWidth = 8f
                            mapView.overlays.add(polyline)
                        }

                        val marker = Marker(mapView)
                        marker.position = location
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        mapView.overlays.add(marker)

                        mapView.invalidate()
                    }
                }
            )

        } else {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Safe Walk", color = Color.White, fontSize = 28.sp)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        walkPath.clear()
                        SafeWalkManager.startSafeWalk(context)
                        isWalking = true
                    }
                ) {
                    Text("Start Safe Walk")
                }
            }
        }

        if (isWalking) {

            Button(
                onClick = {
                    walkPath.clear()
                    SafeWalkManager.stopSafeWalk(context)
                    isWalking = false
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(30.dp)
            ) {
                Text("End Safe Walk")
            }
        }
    }
}