package com.example.shoplist

import android.util.Log
import com.example.shoplist.utils.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.v(TAG(), "Message from: ${message.from}")
        Log.v(
            TAG(),
            "Message data: ${message.data}\n to: ${message.to}\n priority: ${message.priority}"
        )
        Log.v(TAG(), "Notification: ${message.notification}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val user = shopListApp.currentUser()
        user?.getPush(SERVICE_NAME)?.registerDevice(token)
    }
}
