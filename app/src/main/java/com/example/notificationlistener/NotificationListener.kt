package com.example.notificationlistener

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notificationlistener.database.AppDatabase
import com.example.notificationlistener.database.Notification
import com.example.notificationlistener.entity.BankNotification
import com.example.notificationlistener.entity.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.let {
            val notification = it.notification.extras
            val title = notification.getString("android.title") ?: ""
            val text = notification.getString("android.subtext") ?: ""

            if (!title.contains("Compra no crédito")) return

            val bankNotification = BankNotification(
                text = text,
                bank = if (text.contains("com o cartão final")) "inter" else "nubank"
            )

            val expense = Expense(
                name = bankNotification.extractName(),
                value = bankNotification.extractValue(),
                bank = bankNotification.bank,
                card = bankNotification.extractCard(),
                timestamp = getCurrentTimestamp(),
                categoryId = 1
            )

            ApiClient.sendNotification(expense) { success ->
                if (success) {
                    Log.d("NotificationListener", "Notification with data sent successfully!")
                } else {
                    Log.e("NotificationListener", "Failed to send notification with data.")
                }
            }
        }
    }

    private fun saveNotification(
        title: String,
        subtext: String,
        text: String,
        app: String,
        timestamp: String
    ) {
        Log.d("NotificationListener", timestamp)
        val db = AppDatabase.getDatabase(applicationContext)
        val notification = Notification(
            title = title,
            text = text,
            subtext = subtext,
            app = app,
            timestamp = timestamp
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.notificationDao().insertNotification(notification)
            Log.d("NotificationListener", "Notification saved to database")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("NotificationListener", "Notification removed: ${sbn?.packageName}")
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }
}

