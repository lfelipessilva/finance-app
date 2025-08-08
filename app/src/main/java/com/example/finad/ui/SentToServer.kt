package com.example.finad.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
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
        val isRetrying = viewModel.isRetrying

        LaunchedEffect(Unit) {
                viewModel.loadExpenses(context)
                viewModel.checkNotificationAccess(context)
        }

        Column(
                Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp)
        ) {
                // Header
                Text(
                        text = "Sent to Server",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                )

                // Success Summary
                val successfulExpenses = expenses.count { it.success }
                val failedExpenses = expenses.count { !it.success }

                if (expenses.isNotEmpty()) {
                        Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.surfaceVariant
                                        )
                        ) {
                                Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                        text = "$successfulExpenses",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .headlineMedium,
                                                        color = MaterialTheme.colorScheme.primary
                                                )
                                                Text(
                                                        text = "Successful",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant
                                                )
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                        text = "$failedExpenses",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .headlineMedium,
                                                        color = MaterialTheme.colorScheme.error
                                                )
                                                Text(
                                                        text = "Failed",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant
                                                )
                                        }
                                }
                        }
                }

                // Expenses List
                if (expenses.isNotEmpty()) {
                        LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                                items(expenses) { expense ->
                                        SentToServerExpenseItem(
                                                expense = expense,
                                                onRetry = {
                                                        viewModel.retryExpense(expense, context)
                                                },
                                                isRetrying = isRetrying
                                        )
                                }
                        }
                } else {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(
                                        text = "No expenses sent yet",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodyMedium
                                )
                        }
                }

                // Bottom Section
                Column(
                        Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                        // Retry All Failed Button
                        if (failedExpenses > 0) {
                                Button(
                                        onClick = { viewModel.retryAllFailedExpenses(context) },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !isRetrying,
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor =
                                                                MaterialTheme.colorScheme.error,
                                                        contentColor =
                                                                MaterialTheme.colorScheme.onError
                                                )
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "Retry",
                                                modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "Retry All Failed (${failedExpenses})")
                                }
                        }

                        // Notification Status
                        Text(
                                text = notificationStatus,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Enable Notification Access Button
                        OutlinedButton(
                                onClick = {
                                        context.startActivity(
                                                Intent(
                                                        Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                                                )
                                        )
                                },
                                modifier = Modifier.fillMaxWidth()
                        ) { Text(text = "Enable Notification Access") }
                }
        }
}

@Composable
fun SentToServerExpenseItem(
        expense: com.example.finad.data.local.entity.Expense,
        onRetry: () -> Unit,
        isRetrying: Boolean
) {
        Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
                Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Column(modifier = Modifier.weight(1f)) {
                                Text(
                                        text = expense.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                        text = "$${expense.value}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                )
                        }

                        // Status indicator and retry button
                        if (expense.success) {
                                Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Success",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                )
                        } else {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = "Failed",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(20.dp)
                                        )
                                        IconButton(onClick = onRetry, enabled = !isRetrying) {
                                                Icon(
                                                        imageVector = Icons.Default.Refresh,
                                                        contentDescription = "Retry",
                                                        tint = MaterialTheme.colorScheme.error,
                                                        modifier = Modifier.size(16.dp)
                                                )
                                        }
                                }
                        }
                }
        }
}
