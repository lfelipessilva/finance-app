package com.example.finad

import androidx.activity.compose.setContent
import com.example.finad.ui.ExpenseListScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finad.ui.BottomBar
import com.example.finad.ui.SentToServerScreen
import com.example.finad.ui.BottomNavItem
import com.example.finad.ui.ExpenseFilterScreen
import com.example.finad.ui.LoginScreen
import com.example.finad.ui.theme.FinadTheme
import com.example.finad.views.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinadTheme {
                val navController = rememberNavController()
                val expenseViewModel = ExpenseViewModel()

                val bottomNavItems = listOf(
                    BottomNavItem("Gastos", "expense/list", Icons.Default.Done),
                    BottomNavItem("Login", "login", Icons.Default.Done),
                    BottomNavItem("Sent", "sent", Icons.Default.Notifications)
                )

                Scaffold(
                    bottomBar = {
                        BottomBar(navController, bottomNavItems)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(onLoginSuccess = {})
                        }
                        composable("expense/list") {
                            ExpenseListScreen(navController, expenseViewModel)
                        }
                        composable("expense/filter") {
                            ExpenseFilterScreen(navController, expenseViewModel)
                        }
                        composable("sent") {
                            SentToServerScreen()
                        }
                    }
                }
            }
        }
    }
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            NotificationScreen()
//        }
//    }
//
//    @Composable
//    fun NotificationScreen() {
//        val expenses = remember { mutableStateListOf<Expense>() }
//        val notificationStatus = remember { mutableStateOf("Checking notification access...") }
//
//        loadExpenses(expenses)
//        checkNotificationAccessPermission(notificationStatus)
//
//        Column(Modifier.padding(16.dp)) {
//            Text(text = "Notifications", style = MaterialTheme.typography.titleLarge)
//
//            if (expenses.isNotEmpty()) {
//                LazyColumn(
//                    modifier = Modifier.weight(1f),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(expenses) { notification ->
//                        ExpenseItem(notification)
//                    }
//                }
//            } else {
//                Text(text = "No notifications saved.")
//            }
//
//            Column(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(text = notificationStatus.value, style = MaterialTheme.typography.bodyMedium)
//
//                Button(
//                    onClick = {
//                        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
//                    }
//                ) {
//                    Text(text = "Enable Notification Access")
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun ExpenseItem(expense: Expense) {
//        Column(
//            verticalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            Text(text = "nome: ${expense.name}", style = MaterialTheme.typography.bodyLarge)
//            Text(text = "value: ${expense.value}", style = MaterialTheme.typography.bodyMedium)
//            Text(text = "bank: ${expense.bank}", style = MaterialTheme.typography.bodyMedium)
//
//            Row(
//                Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = "app: ${expense.card}", style = MaterialTheme.typography.bodySmall)
//                Text(
//                    text = "hora: ${expense.timestamp}",
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//            HorizontalDivider()
//        }
//    }
//
//    private fun loadExpenses(expenses: MutableList<Expense>) {
//        val database = AppDatabase.getDatabase(this)
//
//        lifecycleScope.launch {
//            val expenseList = database.expenseDao().getAllExpenses()
//
//            withContext(Dispatchers.Main) {
//                expenses.clear()
//                expenses.addAll(expenseList )
//            }
//        }
//    }
//
//    private fun checkNotificationAccessPermission(notificationStatus: MutableState<String>) {
//        val isNotificationAccessGranted =
//            NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)
//
//        if (isNotificationAccessGranted) {
//            notificationStatus.value = "App has access to notifications."
//        } else {
//            notificationStatus.value = "App does not have access to notifications."
//        }
//    }
//}


