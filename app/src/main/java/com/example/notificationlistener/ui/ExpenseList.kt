package com.example.notificationlistener.ui

import ExpenseFilterSheetContent
import FilterExpenseBottomSheet
import android.util.Log
import androidx.compose.foundation.Canvas
import com.example.notificationlistener.data.remote.ExpenseService
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.notificationlistener.data.remote.dto.ListExpenseFilterDto
import com.example.notificationlistener.data.remote.entity.Expense
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen() {
    val listState = rememberLazyListState()
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var filters by remember { mutableStateOf(ListExpenseFilterDto()) }
    var isLoading by remember { mutableStateOf(true) }
    var isFetchingMore by remember { mutableStateOf(false) }
    var endReached by remember { mutableStateOf(false) }

    LaunchedEffect(filters) {
        ExpenseService.getAllExpenses(filters) { success, data ->
            if (success) {
                if (isFetchingMore) {
                    expenses = expenses + data!!.data
                } else {
                    expenses = data!!.data
                    endReached = false
                }

                if (expenses.size == data.summary.total) endReached = true
            }

            isLoading = false
            isFetchingMore = false
        }
    }

    FilterExpenseBottomSheet(
        sheetContent = {
            ExpenseFilterSheetContent(
                initialFilters = filters,
                onApply = { newFilters ->
                    isLoading = true
                    filters = newFilters
                }
            )
        }
    ) { openSheet ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Expenses")
                    },
                    actions = {
                        IconButton(onClick = openSheet) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Open Filter"
                            )
                        }
                    }
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(state = listState) {
                        itemsIndexed(expenses) { index, expense ->
                            ExpenseItem(expense)

                            if (index == expenses.lastIndex && !isFetchingMore && !endReached) {
                                Log.d("Scroll", "End reached")
                                filters = filters.copy(page = filters.page + 1)
                                isFetchingMore = true
                            }
                        }

                        if (!endReached) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(Modifier.padding(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (!expense.category?.color.isNullOrBlank()) {
                    Canvas(modifier = Modifier.size(48.dp)) {
                        drawCircle(color = Color(expense.category.color.toColorInt()))
                    }
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
                Text(
                    text = formatToDate(expense.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatToDate(iso: String): String =
    ZonedDateTime.parse(iso).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

fun formatToCurrency(valueInCents: Int): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return currencyFormatter.format(valueInCents / 100.0)
}
