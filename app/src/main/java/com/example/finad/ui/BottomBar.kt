package com.example.finad.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController, items: List<BottomNavItem>) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = item.page === backStackEntry.value?.destination?.route,
                onClick = { navController.navigate(item.page) }
            )
        }
    }
}

data class BottomNavItem(val label: String, val page: String, val icon: ImageVector)
