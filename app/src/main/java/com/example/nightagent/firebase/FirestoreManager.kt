package com.example.nightagent.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    private var currentSessionId: String? = null

    fun startSOSSession(
        message: String,
        latitude: Double?,
        longitude: Double?
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val sessionData = hashMapOf(
            "message" to message,
            "startLat" to latitude,
            "startLng" to longitude,
            "startTime" to System.currentTimeMillis()
        )

        val docRef = db.collection("sos_alerts")
            .document(userId)
            .collection("sessions")
            .document()

        currentSessionId = docRef.id

        docRef.set(sessionData)
            .addOnSuccessListener {
                Log.d("FIRESTORE_DEBUG", "SOS session started")
            }
            .addOnFailureListener {
                Log.e("FIRESTORE_DEBUG", "Session failed: ${it.message}")
            }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val sessionId = currentSessionId ?: return

        val data = hashMapOf(
            "lat" to latitude,
            "lng" to longitude,
            "time" to System.currentTimeMillis()
        )

        db.collection("sos_alerts")
            .document(userId)
            .collection("sessions")
            .document(sessionId)
            .collection("location_updates")
            .add(data)
            .addOnSuccessListener {
                Log.d("TRACKING", "Location saved")
            }
            .addOnFailureListener {
                Log.e("TRACKING", "Failed: ${it.message}")
            }
    }
}