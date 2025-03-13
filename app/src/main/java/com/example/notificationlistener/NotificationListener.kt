package com.example.notificationlistener

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notificationlistener.database.AppDatabase
import com.example.notificationlistener.database.Notification
import com.example.notificationlistener.entity.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.let {
            val notification = it.notification
            val extras = notification.extras
            val text = extras.getString("android.text") ?: ""
            val title = extras.getString("android.title") ?: ""

            saveNotification(title, text, getCurrentTimestamp())

            if (text.contains("APROVADA", ignoreCase = false)) {
                val value = extractValue(text)
                val name = extractName(text)
                val timestamp = getCurrentTimestamp()

                val notificationData = Expense(name, value,  "Sem categoria",timestamp)
                ApiClient.sendNotification(notificationData) { success ->
                    if (success) {
                        Log.d("NotificationListener", "Notification with data sent successfully!")
                    } else {
                        Log.e("NotificationListener", "Failed to send notification with data.")
                    }
                }
            }
        }
    }

    private fun saveNotification(title: String, text: String, timestamp: String) {
        Log.d("NotificationListener", timestamp)
        val db = AppDatabase.getDatabase(applicationContext)
        val notification = Notification(title = title, text = text, timestamp = timestamp)

        CoroutineScope(Dispatchers.IO).launch {
            db.notificationDao().insertNotification(notification)
            Log.d("NotificationListener", "Notification saved to database")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("NotificationListener", "Notification removed: ${sbn?.packageName}")
    }

    /**
     * Extracts the value from the text like "R$7,95" to "795"
     */
    private fun extractValue(text: String): Int {
        val regex = "R\\$([\\d,]+)".toRegex() // Regex to match R$ followed by numbers with commas
        val match = regex.find(text)
        return match?.groupValues?.get(1)?.replace(",", "")?.toInt() ?: 0
    }

    /**
     * Extracts the name from the text (e.g., "UBER*PENDING")
     */
    private fun extractName(text: String): String {
        val regex = "em\\s([A-Za-z0-9*]+)".toRegex() // Regex to match name after "em"
        val match = regex.find(text)
        return match?.groupValues?.get(1) ?: "Unknown"
    }

    /**
     * Gets the current timestamp in the format yyyy-MM-dd HH:mm:ss
     */
    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }
}

