package com.example.finad.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController, items: List<BottomNavItem>) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .height(56.dp)
                            .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val selected = item.page == backStackEntry.value?.destination?.route
            val contentColor =
                    if (selected) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.onSurfaceVariant

            Column(
                    modifier =
                            Modifier.clickable(
                                            indication = null,
                                            interactionSource =
                                                    remember { MutableInteractionSource() }
                                    ) { if (!selected) navController.navigate(item.page) }
                                    .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = item.icon, contentDescription = item.label, tint = contentColor)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor
                )
            }
        }
    }
}

data class BottomNavItem(val label: String, val page: String, val icon: ImageVector)
