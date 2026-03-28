package com.example.nightagent.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

import com.example.nightagent.sos.LocationProvider
import com.example.nightagent.ui.theme.*

import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun MapScreen() {

    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }

    // Get current location
    LaunchedEffect(Unit) {
        LocationProvider.getLocation(context) { location: Location? ->
            location?.let {
                userLocation = GeoPoint(it.latitude, it.longitude)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {

        if (userLocation != null) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->

                    Configuration.getInstance()
                        .setUserAgentValue(ctx.packageName)

                    val mapView = MapView(ctx)

                    mapView.setTileSource(TileSourceFactory.MAPNIK)
                    mapView.setMultiTouchControls(true)

                    val controller = mapView.controller
                    controller.setZoom(16.0)
                    controller.setCenter(userLocation)

                    // User marker
                    val userMarker = Marker(mapView)
                    userMarker.position = userLocation
                    userMarker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                    userMarker.title = "You are here"

                    mapView.overlays.add(userMarker)

                    // Fetch safety places
                    fetchSafetyPlaces(
                        mapView,
                        userLocation!!.latitude,
                        userLocation!!.longitude,
                        ctx
                    )

                    mapViewRef = mapView

                    mapView
                }
            )

        }

        // Floating buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 120.dp)
        ) {

            // Directions button
            FloatingActionButton(
                onClick = {

                    val uri = Uri.parse(
                        "google.navigation:q=police station near me"
                    )

                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps")

                    context.startActivity(intent)

                },
                containerColor = BlushPink
            ) {
                Icon(Icons.Default.Directions, "Directions")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Locate Me button
            FloatingActionButton(
                onClick = {

                    userLocation?.let {
                        mapViewRef?.controller?.animateTo(it)
                    }

                },
                containerColor = Lavender
            ) {
                Icon(Icons.Default.MyLocation, "Locate Me")
            }

        }

    }

}

fun fetchSafetyPlaces(
    mapView: MapView,
    latitude: Double,
    longitude: Double,
    context: Context
) {

    val url = """
https://overpass-api.de/api/interpreter?data=
[out:json];
(
node["amenity"="police"](around:2000,$latitude,$longitude);
node["amenity"="hospital"](around:2000,$latitude,$longitude);
node["amenity"="cafe"](around:2000,$latitude,$longitude);
node["amenity"="restaurant"](around:2000,$latitude,$longitude);
node["amenity"="fast_food"](around:2000,$latitude,$longitude);
);
out;
""".trimIndent()

    Thread {

        try {

            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val response = connection.inputStream.bufferedReader().readText()

            val json = JSONObject(response)
            val elements = json.getJSONArray("elements")

            for (i in 0 until elements.length()) {

                val place = elements.getJSONObject(i)

                val tags = place.getJSONObject("tags")
                val type = tags.optString("amenity")

                val lat = place.getDouble("lat")
                val lon = place.getDouble("lon")

                val name = tags.optString("name", "Safety Point")

                val marker = Marker(mapView)
                marker.position = GeoPoint(lat, lon)
                marker.title = name

                // Different icons
                when(type){

                    "police" -> marker.icon =
                        ContextCompat.getDrawable(context, android.R.drawable.ic_lock_lock)

                    "hospital" -> marker.icon =
                        ContextCompat.getDrawable(context, android.R.drawable.ic_menu_info_details)

                    else -> marker.icon =
                        ContextCompat.getDrawable(context, android.R.drawable.ic_menu_myplaces)

                }

                // Click marker → open navigation
                marker.setOnMarkerClickListener { m, _ ->

                    val latNav = m.position.latitude
                    val lonNav = m.position.longitude

                    val uri = Uri.parse(
                        "google.navigation:q=$latNav,$lonNav"
                    )

                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps")

                    context.startActivity(intent)

                    true
                }

                mapView.post {
                    mapView.overlays.add(marker)
                    mapView.invalidate()
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }.start()

}