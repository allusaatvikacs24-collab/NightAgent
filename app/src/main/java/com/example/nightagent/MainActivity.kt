package com.example.nightagent

import android.content.Intent
import android.widget.Toast
import com.example.nightagent.sos.LocationProvider
import com.example.nightagent.sos.ShakeDetector
import com.example.nightagent.sos.SOSManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nightagent.navigation.NavGraph
import com.example.nightagent.ui.theme.NightagentTheme

class MainActivity : ComponentActivity() {
    private lateinit var shakeDetector: ShakeDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shakeDetector = ShakeDetector(this) {
            SOSManager.triggerSOS(this)
        }

        shakeDetector.start()

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
}
