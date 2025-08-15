package com.orbita.finad.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.orbita.finad.data.remote.entity.BankNotification

class NotificationListener : NotificationListenerService() {
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NotificationListener", "Notification listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.let {
            val notification = it.notification.extras
            val title = notification.getString("android.title") ?: ""
            val text = notification.getString("android.text") ?: ""

            Log.d("NotificationListener", "New notification posted: title='$title', text='$text'")

            if (title.contains("Compra") || title.contains("Compra aprovada")) {
                val bankNotification =
                        BankNotification(
                                text = text,
                                context = applicationContext,
                                bank =
                                        when {
                                            text.contains("com o cartão final") -> "Inter"
                                            text.contains("para o cartão com final") -> "Nubank"
                                            text.contains("Compra no cartão final") -> "Santander"
                                            else -> "Unknown"
                                        }
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
