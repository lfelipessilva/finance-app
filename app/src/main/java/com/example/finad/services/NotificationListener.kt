package com.example.finad.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.finad.data.remote.entity.BankNotification

class NotificationListener : NotificationListenerService() {
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NotificationListener", "Notification listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.let {
            // val notification = it.notification.extras
            // val title = notification.getString("android.title") ?: ""
            // val text = notification.getString("android.text") ?: ""

            val title = "Compra no crédito aprovada"
            val text = "Compra de R$ 25,34 APROVADA em AMAZON BR para o cartão com final 3576."

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
