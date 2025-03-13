package com.example.notificationlistener

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.notificationlistener.database.AppDatabase
import com.example.notificationlistener.database.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationScreen()
        }
    }

    @Composable
    fun NotificationScreen() {
        val notifications = remember { mutableStateListOf<Notification>() }
        val notificationStatus = remember { mutableStateOf("Checking notification access...") }

        loadNotifications(notifications)
        checkNotificationAccessPermission(notificationStatus)

        Column(Modifier.padding(16.dp)) {
            Text(text = "Notifications", style = MaterialTheme.typography.titleLarge)

            if (notifications.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationItem(notification)
                    }
                }
            } else {
                Text(text = "No notifications saved.")
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = notificationStatus.value, style = MaterialTheme.typography.bodyMedium)

                Button(
                    onClick = {
                        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                    }
                ) {
                    Text(text = "Enable Notification Access")
                }
            }
        }
    }

    @Composable
    fun NotificationItem(notification: Notification) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "titulo: ${notification.title}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "texto: ${notification.text}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "subtexto: ${notification.subtext}", style = MaterialTheme.typography.bodyMedium)

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "app: ${notification.app}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "hora: ${notification.timestamp}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            HorizontalDivider()
        }
    }

    private fun loadNotifications(notifications: MutableList<Notification>) {
        val database = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val notificationList = database.notificationDao().getAllNotifications()

            withContext(Dispatchers.Main) {
                notifications.clear()
                notifications.addAll(notificationList)
            }
        }
    }

    private fun checkNotificationAccessPermission(notificationStatus: MutableState<String>) {
        val isNotificationAccessGranted =
            NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)

        if (isNotificationAccessGranted) {
            notificationStatus.value = "App has access to notifications."
        } else {
            notificationStatus.value = "App does not have access to notifications."
        }
    }
}


