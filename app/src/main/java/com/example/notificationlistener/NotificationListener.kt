package com.example.notificationlistener

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notificationlistener.database.AppDatabase
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
    override fun onListenerConnected() {
        super.onListenerConnected()

        val notifications = getActiveNotifications()
        notifications.forEach {

            val notification = it.notification.extras
            val title = notification.getString("android.title") ?: ""
            val text = notification.getString("android.text") ?: ""


            if (title.contains("Compra no crédito")) {
                val bankNotification = BankNotification(
                    text = text,
                    bank = if (text.contains("com o cartão final")) "Inter" else "Nubank"
                )

                val expense = Expense(
                    name = bankNotification.extractName(),
                    value = bankNotification.extractValue(),
                    bank = bankNotification.bank,
                    card = bankNotification.extractCard(),
                    timestamp = getCurrentTimestamp(),
                    categoryId = 1
                )

                Log.d("ActiveNotification", expense.toString())
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn?.let {
            val notification = it.notification.extras
            val title = notification.getString("android.title") ?: ""
            val text = notification.getString("android.text") ?: ""

            if (title.contains("Compra no crédito")) {
                val bankNotification = BankNotification(
                    text = text,
                    bank = if (text.contains("com o cartão final")) "Inter" else "Nubank"
                )

                val expense = Expense(
                    name = bankNotification.extractName(),
                    value = bankNotification.extractValue(),
                    bank = bankNotification.bank,
                    card = bankNotification.extractCard(),
                    timestamp = getCurrentTimestamp(),
                    categoryId = 1
                )

                saveExpense(
                    name = expense.name,
                    value = expense.value,
                    bank = expense.bank,
                    card = expense.card,
                    timestamp = expense.timestamp,
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
    }

    private fun saveExpense(
        name: String,
        value: Int,
        bank: String,
        card: String,
        timestamp: String
    ) {
        val db = AppDatabase.getDatabase(applicationContext)
        val expense = com.example.notificationlistener.database.Expense(
            name = name,
            value = value,
            bank = bank,
            card = card,
            timestamp = timestamp
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.expenseDao().insertExpense(expense)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }
}

