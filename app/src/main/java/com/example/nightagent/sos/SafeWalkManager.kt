package com.example.nightagent.sos

import android.content.Context
import android.location.Location
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.Manifest
import android.os.Handler
import android.os.Looper
import kotlin.math.abs

import com.example.nightagent.contacts.ContactManager
import com.example.nightagent.repository.SafeWalkRepository
import com.example.nightagent.model.LocationModel
import com.example.nightagent.sos.SOSManager
import com.example.nightagent.firebase.AuthManager
import com.example.nightagent.sos.LocationProvider

import kotlinx.coroutines.*

object SafeWalkManager {

    private var isActive = false
    private val repo = SafeWalkRepository()
    private val handler = Handler(Looper.getMainLooper())

    private suspend fun getUserId(): String = AuthManager.getCurrentUserId() ?: "anonymous_user"

    private var startTime: Long = 0
    private var lastLocation: Location? = null
    private var startLocation: Location? = null
    private var lastMoveTime: Long = 0
    private var lastBearing: Float = 0f

    private const val MAX_DURATION = 5 * 60 * 1000L // 5 min
    private const val GRACE_PERIOD = 60 * 1000L // 60 seconds
    private const val NO_MOVEMENT_LIMIT = 120 * 1000L // 2 minutes
    private const val MAX_DISTANCE_FROM_START = 300.0f // meters
    private const val DEVIATION_ANGLE_THRESHOLD = 60f
    private const val DEVIATION_DISTANCE_THRESHOLD = 10f

    fun startSafeWalk(context: Context) {

        if (isActive) return
        isActive = true

        startTime = System.currentTimeMillis()
        lastMoveTime = startTime
        lastBearing = 0f

        // 🔥 Save session to Firebase
        CoroutineScope(Dispatchers.IO).launch {
            val uid = getUserId()
            repo.startSafeWalk(uid, startTime)
        }

        // 📍 Capture start location
        LocationProvider.getLocation(context) { location ->
            if (location != null) {
                startLocation = location
            }
        }

        sendStartSMS(context)
        startMonitoring(context)
    }

    private fun sendStartSMS(context: Context) {

        LocationProvider.getLocation(context) { location ->

            if (location == null) return@getLocation

            val message = """
Safe Walk Started

Track my location:
https://maps.google.com/?q=${location.latitude},${location.longitude}
""".trimIndent()

            val contacts = ContactManager.getContacts(context)
            val smsManager = SmsManager.getDefault()

            for (contact in contacts) {

                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.SEND_SMS
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    smsManager.sendTextMessage(contact.phone, null, message, null, null)
                }
            }

            Toast.makeText(context, "Safe Walk started", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMonitoring(context: Context) {

        handler.post(object : Runnable {
            override fun run() {
                if (!isActive) return

                LocationProvider.getLocation(context) { location ->

                    if (location == null) {
                        handler.postDelayed(this, 5000)
                        return@getLocation
                    }

                    checkMovement(context, location)
                    checkDeviation(context, location)
                    saveLocation(location)
                    checkTimeout(context)
                }

                // 🔁 Repeat every 5 sec
                handler.postDelayed(this, 5000)
            }
        })
    }

    private fun saveLocation(location: Location) {

        val data = LocationModel(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = System.currentTimeMillis()
        )

        CoroutineScope(Dispatchers.IO).launch {
            val uid = getUserId()
            repo.updateLocation(uid, data)
        }
    }

    private fun checkMovement(context: Context, newLocation: Location) {

        // Handle first location
        if (lastLocation == null) {
            lastLocation = newLocation
            lastMoveTime = System.currentTimeMillis()
            return
        }

        val distance = lastLocation!!.distanceTo(newLocation)
        
        // Calculate bearing deviation
        val newBearing = lastLocation!!.bearingTo(newLocation)
        val bearingDiff = abs(newBearing - lastBearing)
        lastBearing = newBearing
        
        lastLocation = newLocation

        if (distance > 5.0f) {
            // Movement detected
            lastMoveTime = System.currentTimeMillis()
        } else {
            // No significant movement
            val noMoveDuration = System.currentTimeMillis() - lastMoveTime
            val elapsedTime = System.currentTimeMillis() - startTime
            if (noMoveDuration > NO_MOVEMENT_LIMIT && elapsedTime >= GRACE_PERIOD) {
                triggerSOS(context, "No movement for 120 seconds")
            }
        }
        
        // Check for sharp deviation
        if (distance > DEVIATION_DISTANCE_THRESHOLD && bearingDiff > DEVIATION_ANGLE_THRESHOLD) {
            triggerAutoSOS(context, "Sharp deviation detected (${bearingDiff.toInt()}°)")
        }
    }

    private fun checkDeviation(context: Context, current: Location) {

        if (startLocation == null) return

        val distanceFromStart = startLocation!!.distanceTo(current)

        if (distanceFromStart > MAX_DISTANCE_FROM_START) {
            triggerSOS(context, "Moved too far from start location")
        }
    }

    private fun checkTimeout(context: Context) {

        val elapsed = System.currentTimeMillis() - startTime

        if (elapsed > MAX_DURATION) {
            triggerSOS(context, "Time limit exceeded")
        }
    }

    private fun triggerSOS(context: Context, reason: String) {

        if (!isActive) return

        isActive = false
        handler.removeCallbacksAndMessages(null)

        Toast.makeText(context, "Unsafe: $reason", Toast.LENGTH_LONG).show()

        SOSManager.triggerSOS(context)
    }
    
    private fun triggerAutoSOS(context: Context, reason: String) {

        if (!isActive) return

        isActive = false
        handler.removeCallbacksAndMessages(null)

        Toast.makeText(context, "Deviation Alert: $reason", Toast.LENGTH_LONG).show()

        SOSManager.triggerSOS(context)
    }

    fun stopSafeWalk(context: Context) {

        if (!isActive) return
        isActive = false
        handler.removeCallbacksAndMessages(null)

        CoroutineScope(Dispatchers.IO).launch {
            val uid = getUserId()
            repo.endSafeWalk(uid)
        }

        Toast.makeText(context, "Safe Walk ended", Toast.LENGTH_SHORT).show()
    }
}
