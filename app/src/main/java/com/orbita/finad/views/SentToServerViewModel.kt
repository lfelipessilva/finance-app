package com.orbita.finad.views

import android.content.Context
import androidx.compose.runtime.*
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbita.finad.data.AppDatabase
import com.orbita.finad.data.local.entity.Expense
import com.orbita.finad.data.remote.ExpenseService
import com.orbita.finad.data.remote.dto.CreateExpenseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SentToFinadViewModel : ViewModel() {
    var expenses = mutableStateListOf<Expense>()
        private set

    var notificationStatus by mutableStateOf("Checking notification access...")
        private set

    var isRetrying by mutableStateOf(false)
        private set

    fun loadExpenses(context: Context) {
        val db = AppDatabase.getDatabase(context)
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) { db.expenseDao().getAllExpenses() }
            expenses.clear()
            expenses.addAll(list)
        }
    }

    fun checkNotificationAccess(context: Context) {
        val hasAccess =
                NotificationManagerCompat.getEnabledListenerPackages(context)
                        .contains(context.packageName)

        notificationStatus =
                if (hasAccess) {
                    "App has access to notifications."
                } else {
                    "App does not have access to notifications."
                }
    }

    fun retryExpense(expense: Expense, context: Context) {
        isRetrying = true

        val createExpenseDto =
                CreateExpenseDto(
                        name = expense.name,
                        value = expense.value,
                        bank = expense.bank,
                        card = expense.card,
                        categoryId = null,
                        description = null,
                        timestamp = expense.timestamp,
                )

        ExpenseService.createExpense(createExpenseDto) { success ->
            viewModelScope.launch {
                val db = AppDatabase.getDatabase(context)

                if (success) {
                    // Update local expense to mark as successful
                    withContext(Dispatchers.IO) {
                        db.expenseDao().updateExpenseSuccess(expense.id, true)
                    }

                    // Reload expenses to reflect the change
                    loadExpenses(context)
                }

                isRetrying = false
            }
        }
    }

    fun retryAllFailedExpenses(context: Context) {
        val failedExpenses = expenses.filter { !it.success }

        failedExpenses.forEach { expense -> retryExpense(expense, context) }
    }
}
