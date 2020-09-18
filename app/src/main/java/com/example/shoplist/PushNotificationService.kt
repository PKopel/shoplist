package com.example.shoplist

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

        val intent = Intent(this, ShopListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.web_hi_res_512)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val user = shopListApp.currentUser()
        user?.getPush(SERVICE_NAME)?.registerDevice(token)
    }
}
