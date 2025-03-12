package com.example.notificationlistener

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvNotificationStatus = findViewById<TextView>(R.id.tvNotificationStatus)
        val btnEnableNotifications = findViewById<Button>(R.id.btnEnableNotifications)

        // Check notification access when the activity is created
        checkNotificationAccessPermission(tvNotificationStatus)

        // Button click to open notification listener settings
        btnEnableNotifications.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    // Method to check notification access permission
    private fun checkNotificationAccessPermission(tvNotificationStatus: TextView) {
        val isNotificationAccessGranted =
            NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)

        if (isNotificationAccessGranted) {
            tvNotificationStatus.text = "App has access to notifications."
        } else {
            tvNotificationStatus.text = "App does not have access to notifications."
        }
    }

    // Override onResume to update the UI when the app comes back into the foreground
    override fun onResume() {
        super.onResume()
        val tvNotificationStatus = findViewById<TextView>(R.id.tvNotificationStatus)
        checkNotificationAccessPermission(tvNotificationStatus)
    }
}
