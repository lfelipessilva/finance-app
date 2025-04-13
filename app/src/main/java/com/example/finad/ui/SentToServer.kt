// SentToFinadScreen.kt
package com.example.finad.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finad.views.SentToFinadViewModel

@Composable
fun SentToServerScreen() {
    val context = LocalContext.current
    val viewModel: SentToFinadViewModel = viewModel()
    val expenses = viewModel.expenses
    val notificationStatus = viewModel.notificationStatus

    LaunchedEffect(Unit) {
        viewModel.loadExpenses(context)
        viewModel.checkNotificationAccess(context)
    }

    Column(Modifier.padding(16.dp)) {
        Text(text = "Notifications", style = MaterialTheme.typography.titleLarge)

        if (expenses.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(expenses) { notification ->
                    SentToServerExpenseItem(notification)
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
            Text(text = notificationStatus, style = MaterialTheme.typography.bodyMedium)

            Button(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }
            ) {
                Text(text = "Enable Notification Access")
            }
        }
    }
}

@Composable
fun SentToServerExpenseItem(expense: com.example.finad.data.local.entity.Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = expense.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "$${expense.value}")
            Text(text = "Bank: ${expense.bank} | Card: ${expense.card}")
        }
    }
}
