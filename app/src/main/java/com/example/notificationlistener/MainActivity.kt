package com.example.notificationlistener

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.example.notificationlistener.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvNotificationStatus = findViewById<TextView>(R.id.tvNotificationStatus)
        val tvNotificationData = findViewById<TextView>(R.id.tvNotificationData)
        val btnEnableNotifications = findViewById<Button>(R.id.btnEnableNotifications)

        // Check notification access when the activity is created
        checkNotificationAccessPermission(tvNotificationStatus)

        // Button click to open notification listener settings
        btnEnableNotifications.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        // Load saved notifications from Room database
        loadNotifications(tvNotificationData)
    }

    // Method to check notification access permission
    @SuppressLint("SetTextI18n")
    private fun checkNotificationAccessPermission(tvNotificationStatus: TextView) {
        val isNotificationAccessGranted =
            NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)

        if (isNotificationAccessGranted) {
            tvNotificationStatus.text = "App has access to notifications."
        } else {
            tvNotificationStatus.text = "App does not have access to notifications."
        }
    }

    // Fetch and display notifications from the database
    private fun loadNotifications(tvNotificationData: TextView) {
        val database = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            val notifications = database.notificationDao().getAllNotifications()

            withContext(Dispatchers.Main) {
                if (notifications.isNotEmpty()) {
                    val formattedData = notifications.joinToString("\n") {
                        "${it.timestamp}: ${it.title} - ${it.text}"
                    }
                    tvNotificationData.text = formattedData
                } else {
                    tvNotificationData.text = "No notifications saved."
                }
            }
        }
    }

    // Override onResume to update the UI when the app comes back into the foreground
    override fun onResume() {
        super.onResume()
        val tvNotificationStatus = findViewById<TextView>(R.id.tvNotificationStatus)
        val tvNotificationData = findViewById<TextView>(R.id.tvNotificationData)

        checkNotificationAccessPermission(tvNotificationStatus)
        loadNotifications(tvNotificationData)
    }
}

