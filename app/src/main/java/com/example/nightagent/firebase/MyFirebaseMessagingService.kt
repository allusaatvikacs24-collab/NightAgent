package com.example.nightagent.firebase

import android.util.Log
import com.example.nightagent.notifications.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFCMService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "FCM message received: ${remoteMessage.data}")

        // Handle foreground notifications
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.notification?.title ?: "NightAgent Alert"
            val body = remoteMessage.notification?.body ?: "New notification received"
            NotificationHelper.show(this, "$title: $body")
        }

        // Handle data payload if needed
        // TODO: Process remoteMessage.data for app-specific logic
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM token refreshed: $token")
        // Send token to your server if needed
    }
}
