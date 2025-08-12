package com.orbita.finad.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orbita.finad.views.SentToFinadViewModel

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

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(12.dp)) {
        val failedExpenses = expenses.count { !it.success }

        if (expenses.isNotEmpty()) {
            LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
                    SentToServerExpenseItem(
                            expense = expense,
                            onRetry = { viewModel.retryExpense(expense, context) },
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

        Column(
                Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (failedExpenses > 0) {
                Button(
                        onClick = { viewModel.retryAllFailedExpenses(context) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isRetrying,
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
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
        }
    }
}

@Composable
fun SentToServerExpenseItem(
        expense: com.orbita.finad.data.local.entity.Expense,
        onRetry: () -> Unit,
        isRetrying: Boolean
) {
    Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
        ) {
            Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
            ) {
                Box(
                        modifier =
                                Modifier.clip(CircleShape)
                                        .size(48.dp)
                                        .background(
                                                if (expense.success)
                                                        MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.error
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            imageVector =
                                    if (expense.success) Icons.Default.CheckCircle
                                    else Icons.Default.Warning,
                            contentDescription = if (expense.success) "Success" else "Failed",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                    )
                }

                Column() {
                    Text(
                            text = expense.name,
                            style = MaterialTheme.typography.titleMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                    )
                    Text(text = expense.bank, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                        text = formatToCurrency(expense.value),
                        style = MaterialTheme.typography.titleMedium
                )
                if (!expense.success) {
                    IconButton(
                            onClick = onRetry,
                            enabled = !isRetrying,
                            modifier = Modifier.size(32.dp)
                    ) {
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
