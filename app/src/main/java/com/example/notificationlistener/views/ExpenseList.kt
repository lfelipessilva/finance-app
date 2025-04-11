package com.example.notificationlistener.views

import com.example.notificationlistener.data.remote.ExpenseService
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notificationlistener.data.remote.entity.Expense

@Composable
fun ExpenseListScreen(onNavigateToSent: () -> Unit) {
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        ExpenseService.getAllExpenses { success, data ->
            if (success) expenses = data
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
             Text("All Expenses")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToSent) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)) {

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(expenses) { expense ->
                        ExpenseItem(expense)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
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
