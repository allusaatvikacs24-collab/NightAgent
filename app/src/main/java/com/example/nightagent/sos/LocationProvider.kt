package com.example.nightagent.sos

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

object LocationProvider {

    @SuppressLint("MissingPermission")
    fun getLocation(
        context: Context,
        callback: (Location?) -> Unit
    ) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                callback(location)
            }

    }
}