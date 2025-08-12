package com.orbita.finad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.orbita.finad.data.local.SessionManager
import com.orbita.finad.data.remote.ApiClient
import com.orbita.finad.ui.ExpenseFilterScreen
import com.orbita.finad.ui.LoginScreen
import com.orbita.finad.ui.SentToServerScreen
import com.orbita.finad.ui.component.BottomBar
import com.orbita.finad.ui.component.BottomNavItem
import com.orbita.finad.ui.expense.CreateExpenseScreen
import com.orbita.finad.ui.expense.EditExpenseScreen
import com.orbita.finad.ui.expense.ExpenseListScreen
import com.orbita.finad.ui.theme.FinadTheme
import com.orbita.finad.views.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiClient.initialize(this)

        setContent { FinadTheme { MainContent() } }
    }
}

@Composable
fun MainContent() {
    val context = LocalContext.current
    val sessionManager = SessionManager.getInstance(context)
    val isLoggedIn by sessionManager.isLoggedInFlow.collectAsState()
    val navController = rememberNavController()
    val expenseViewModel = ExpenseViewModel()

    if (!isLoggedIn) {
        LoginScreen(onLoginSuccess = {})
    } else {
        val bottomNavItems =
                listOf(
                    BottomNavItem("Gastos", "expense/list", Icons.Default.Done),
                        BottomNavItem("Criar", "expense/create", Icons.Default.Done),
                        BottomNavItem("Sent", "sent", Icons.Default.Notifications)
                )

        Scaffold(bottomBar = { BottomBar(navController, bottomNavItems) }) { innerPadding ->
            NavHost(
                    navController = navController,
                    startDestination = "expense/list",
                    modifier = Modifier.padding(innerPadding)
            ) {
                composable("expense/list") {
                    ExpenseListScreen(navController, expenseViewModel, sessionManager)
                }
                composable("expense/create") {
                    CreateExpenseScreen(navController, expenseViewModel)
                }
                composable("expense/filter") {
                    ExpenseFilterScreen(navController, expenseViewModel)
                }
                composable(
                        route = "expense/edit/{expenseId}",
                        arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val expenseId = backStackEntry.arguments?.getInt("expenseId")
                    if (expenseId != null) {
                        EditExpenseScreen(navController, expenseViewModel, expenseId)
                    }
                }
                composable("sent") { SentToServerScreen() }
            }
        }
    }
}
