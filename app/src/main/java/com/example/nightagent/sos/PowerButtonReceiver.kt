package com.example.nightagent.sos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class PowerButtonReceiver : BroadcastReceiver() {

    private var pressCount = 0
    private var lastPressTime = 0L

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(context, "Receiver triggered", Toast.LENGTH_SHORT).show()

        // Toggle check
        if (!SafetySettings.powerButtonSOS.value) return

        val now = System.currentTimeMillis()

        if (now - lastPressTime < 2000) {
            pressCount++
        } else {
            pressCount = 1
        }

        lastPressTime = now

        if (pressCount >= 2) {

            pressCount = 0

            Toast.makeText(context, "Power SOS triggered", Toast.LENGTH_SHORT).show()


        }
    }
}