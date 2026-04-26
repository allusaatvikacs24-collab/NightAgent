package com.example.nightagent.sos

import android.Manifest
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*

object LocationProvider {

    fun getLocation(
        context: Context,
        callback: (Location?) -> Unit
    ) {

        // 🔥 Check Google Play Services
        val resultCode = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context)

        if (resultCode != ConnectionResult.SUCCESS) {
            Log.e("LocationProvider", "Google Play Services unavailable")
            callback(null)
            return
        }

        // 🔥 Check permission
        val hasPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            Log.e("LocationProvider", "Location permission not granted")
            callback(null)
            return
        }

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(15000)
            .build()

        fusedLocationClient.getCurrentLocation(
            request,
            com.google.android.gms.tasks.CancellationTokenSource().token
        )
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("LocationProvider", "Location: ${location.latitude}, ${location.longitude}")
                    callback(location)
                } else {
                    Log.e("LocationProvider", "Location is NULL")
                    callback(null)
                }
            }
            .addOnFailureListener {
                Log.e("LocationProvider", "Location failed: ${it.message}")
                callback(null)
            }
    }
}