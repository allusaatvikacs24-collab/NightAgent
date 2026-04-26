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

    /**
     * Handle FCM payload - extracts title/body from data or notification
     */
    fun handleFCMPayload(context: Context, data: Map<String, String>?, notification: com.google.firebase.messaging.RemoteMessage.Notification?) {
        if (!SafetySettings.pushNotifications.value) return

        val title = data?.get("title") ?: notification?.title ?: "Notification"
        val body = data?.get("body") ?: notification?.body ?: "New message received"
        show(context, "$title: $body")
    }
}
