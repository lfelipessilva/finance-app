package com.example.notificationlistener

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notificationlistener.entity.Expense
import java.text.SimpleDateFormat
import java.util.*

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.let {
            val packageName = it.packageName
            val notification = it.notification
            val extras = notification.extras
            val title = extras.getString("android.title") ?: "No Title"
            val text = extras.getString("android.text") ?: "No Text"

            Log.d("NotificationListener", "Notification from $packageName: $title - $text")

            // Check if the notification text contains the word "APROVADA"
            if (text.contains("APROVADA", ignoreCase = false)) {
                // Extract the value, name, and timestamp
                val value = extractValue(text)
                val name = extractName(text)
                val timestamp = getCurrentTimestamp()

                // Log the extracted data
                Log.d("NotificationListener", "Extracted Value: $value")
                Log.d("NotificationListener", "Extracted Name: $name")
                Log.d("NotificationListener", "Timestamp: $timestamp")

                // Send the extracted data to the server
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
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}

