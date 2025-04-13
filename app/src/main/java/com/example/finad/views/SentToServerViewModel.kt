package com.example.finad.views

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.core.app.NotificationManagerCompat
import com.example.finad.data.AppDatabase
import com.example.finad.data.local.entity.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SentToFinadViewModel : ViewModel() {
    var expenses = mutableStateListOf<Expense>()
        private set

    var notificationStatus by mutableStateOf("Checking notification access...")
        private set

    fun loadExpenses(context: Context) {
        val db = AppDatabase.getDatabase(context)
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                db.expenseDao().getAllExpenses()
            }
            expenses.clear()
            expenses.addAll(list)
        }
    }

    fun checkNotificationAccess(context: Context) {
        val hasAccess = NotificationManagerCompat.getEnabledListenerPackages(context)
            .contains(context.packageName)

        notificationStatus = if (hasAccess) {
            "App has access to notifications."
        } else {
            "App does not have access to notifications."
        }
    }
}
