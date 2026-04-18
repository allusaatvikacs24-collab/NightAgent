package com.example.nightagent.notifications

import android.content.Context
import android.widget.Toast
import com.example.nightagent.sos.SafetySettings

object NotificationHelper {

    fun show(context: Context, message: String) {

        // If notifications disabled → do nothing
        if (!SafetySettings.pushNotifications.value) return

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}