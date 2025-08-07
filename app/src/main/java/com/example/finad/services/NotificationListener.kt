package com.example.finad.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.finad.data.remote.entity.BankNotification

class NotificationListener : NotificationListenerService() {
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NotificationListener", "Notification listener connected")

        val notifications = getActiveNotifications()
        Log.d("NotificationListener", "Found ${notifications.size} active notifications")

        notifications.forEach { sbn ->
            val notification = sbn.notification.extras
            val title = notification.getString("android.title") ?: ""
            val text = notification.getString("android.text") ?: ""

            Log.d("NotificationListener", "Processing notification: title='$title', text='$text'")

            if (title.contains("Compra no crédito")) {
                Log.d("NotificationListener", "Found credit purchase notification")
                val bankNotification =
                        BankNotification(
                                text = text,
                                context = applicationContext,
                                bank =
                                        if (text.contains("com o cartão final")) "Inter"
                                        else "Nubank"
                        )

                bankNotification.sendToServer()
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.let {
            val notification = it.notification.extras
            val title = notification.getString("android.title") ?: ""
            val text = notification.getString("android.text") ?: ""

            Log.d("NotificationListener", "New notification posted: title='$title', text='$text'")

            if (title.contains("Compra no crédito")) {
                Log.d("NotificationListener", "Found credit purchase notification")
                val bankNotification =
                        BankNotification(
                                text = text,
                                context = applicationContext,
                                bank =
                                        if (text.contains("com o cartão final")) "Inter"
                                        else "Nubank"
                        )

                bankNotification.sendToServer()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("NotificationListener", "Notification removed")
    }
}
