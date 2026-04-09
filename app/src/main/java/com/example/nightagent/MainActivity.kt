package com.example.nightagent

import com.example.nightagent.sos.VoiceSOSManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.nightagent.navigation.NavGraph
import com.example.nightagent.sos.LocationProvider
import com.example.nightagent.sos.PowerButtonReceiver
import com.example.nightagent.sos.ShakeDetector
import com.example.nightagent.sos.SOSManager
import com.example.nightagent.sos.SafetySettings
import com.example.nightagent.ui.theme.NightagentTheme

class MainActivity : ComponentActivity() {

    private lateinit var voiceSOS: VoiceSOSManager

    private lateinit var shakeDetector: ShakeDetector
    private lateinit var powerReceiver: PowerButtonReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Shake Detector
        shakeDetector = ShakeDetector(this) {
            SOSManager.triggerSOS(this)
        }

        shakeDetector.start()

        voiceSOS = VoiceSOSManager(this) {

            if (SafetySettings.voiceSOS.value) {
                SOSManager.triggerSOS(this)
            }
        }

        voiceSOS.startListening()

        // Register Power Button Receiver dynamically
        powerReceiver = PowerButtonReceiver()

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }

        registerReceiver(powerReceiver, filter)

        setContent {
            NightagentTheme {

                NavGraph(
                    onShareLocationClick = {

                        Toast.makeText(this, "Share Location clicked", Toast.LENGTH_SHORT).show()

                        LocationProvider.getLocation(this) { location ->

                            if (location != null) {

                                val link =
                                    "https://maps.google.com/?q=${location.latitude},${location.longitude}"

                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "My current location:\n$link"
                                    )
                                }

                                startActivity(
                                    Intent.createChooser(intent, "Share Location")
                                )

                            } else {

                                Toast.makeText(
                                    this,
                                    "Unable to get location",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerReceiver)
    }
}