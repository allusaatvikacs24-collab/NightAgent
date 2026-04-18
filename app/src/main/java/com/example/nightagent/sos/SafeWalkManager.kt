package com.example.nightagent.sos

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import com.example.nightagent.contacts.ContactManager

object SafeWalkManager {

    fun startSafeWalk(context: Context) {

        LocationProvider.getLocation(context) { location ->

            if (location != null) {

                val lat = location.latitude
                val lon = location.longitude

                val message = """
Safe Walk Started

Track my location:
https://maps.google.com/?q=$lat,$lon
""".trimIndent()

                val contacts = ContactManager.getContacts(context)

                if (contacts.isEmpty()) {

                    Toast.makeText(
                        context,
                        "No emergency contacts added",
                        Toast.LENGTH_LONG
                    ).show()

                    return@getLocation
                }

                try {

                    val smsManager = SmsManager.getDefault()

                    for (contact in contacts) {

                        smsManager.sendTextMessage(
                            contact.phone,
                            null,
                            message,
                            null,
                            null
                        )

                    }

                    Toast.makeText(
                        context,
                        "Safe Walk started. Contacts notified.",
                        Toast.LENGTH_LONG
                    ).show()

                } catch (e: Exception) {

                    Toast.makeText(
                        context,
                        "SMS failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()

                }

            } else {

                Toast.makeText(
                    context,
                    "Location unavailable",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }

    fun stopSafeWalk(context: Context) {

        Toast.makeText(
            context,
            "Safe Walk ended",
            Toast.LENGTH_LONG
        ).show()

    }

}