package com.example.nightagent.sos


import com.example.nightagent.notifications.NotificationHelper
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.widget.Toast
import com.example.nightagent.R
import com.example.nightagent.contacts.ContactManager

object SOSManager {

    private var mediaPlayer: MediaPlayer? = null

    private var sosPending = false

    fun triggerSOS(context: Context) {

        // 🔊 Play alarm ONLY if Silent Mode is OFF
        if (!SafetySettings.silentMode.value) {

            NotificationHelper.show(context, "SOS Triggered")

            mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sos)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }

        // 🎤 Start recording if enabled
        if (SafetySettings.autoRecording.value) {
            EvidenceRecorder.startRecording(context)
        }

        // ⏱ Stop recording after 30 sec
        Handler(Looper.getMainLooper()).postDelayed({

            if (SafetySettings.autoRecording.value) {
                EvidenceRecorder.stopRecording(context)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                EvidenceUploader.uploadRecording(context)
            }, 3000)

        }, 30000)

        // 🔹 If location sharing OFF → send SMS without location
        if (!SafetySettings.locationSharing.value) {

            val message = "HELP! I am in danger. Please contact me immediately."

            val emergencyContacts = ContactManager.getContacts(context)

            val smsManager = SmsManager.getDefault()

            for (contact in emergencyContacts) {
                smsManager.sendTextMessage(
                    contact.phone,
                    null,
                    message,
                    null,
                    null
                )
            }

            return
        }

        // 📍 If location sharing ON → fetch location
        LocationProvider.getLocation(context) { location ->

            if (location != null) {

                val lat = location.latitude
                val lon = location.longitude

                val message = """
HELP! I am in danger.
My location:
https://maps.google.com/?q=$lat,$lon
""".trimIndent()

                val emergencyContacts = ContactManager.getContacts(context)

                if (emergencyContacts.isEmpty()) {

                    if (!SafetySettings.silentMode.value) {
                        NotificationHelper.show(context, "No emergency contacts added")
                    }

                    stopAlarm()
                    return@getLocation
                }

                try {

                    val smsManager = SmsManager.getDefault()

                    for (contact in emergencyContacts) {
                        smsManager.sendTextMessage(
                            contact.phone,
                            null,
                            message,
                            null,
                            null
                        )
                    }

                    if (!SafetySettings.silentMode.value) {
                        NotificationHelper.show(context, "SOS SMS Sent!")
                    }

                } catch (e: Exception) {

                    NotificationHelper.show(
                        context,
                        "SMS failed: ${e.message}")
                }

            } else {

                if (!SafetySettings.silentMode.value) {
                    NotificationHelper.show(context, "Could not get location")
                }

            }

            // 🔊 Stop alarm after sending SMS
            Handler(Looper.getMainLooper()).postDelayed({
                stopAlarm()
            }, 10000)
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}