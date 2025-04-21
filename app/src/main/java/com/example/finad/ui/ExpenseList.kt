package com.example.finad.ui

import androidx.compose.foundation.background
import com.example.finad.data.remote.ExpenseService
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.example.finad.data.remote.dto.ListExpenseFilterDto
import com.example.finad.data.remote.entity.Expense
import com.example.finad.data.remote.entity.ExpenseByCategory
import com.example.finad.ui.component.ExpenseByCategoryList
import com.example.finad.ui.component.SvgIcon
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(navController: NavController) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val listState = rememberLazyListState()
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var total by remember { mutableIntStateOf(0) }
    var expensesByCategory by remember { mutableStateOf<List<ExpenseByCategory>>(emptyList()) }
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

                total = data.sum
                if (expenses.size == data.summary.total) endReached = true
            }

            isLoading = false
            isFetchingMore = false
        }

        ExpenseService.getAllExpensesByCategory(filters.copy(category = null)) { success, data ->
            if (success) {
                expensesByCategory = data!!.data
            }
            isLoading = false
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = (screenHeight - (142.dp + (expensesByCategory.size * 28).dp)),
                sheetContainerColor = MaterialTheme.colorScheme.background,
                sheetContent = {
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        ) {
                            itemsIndexed(expenses) { index, expense ->
                                ExpenseItem(expense, onClick= { id -> navController.navigate("expenses/$id")})

                                if (index == expenses.lastIndex && !isFetchingMore && !endReached) {
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
            ) {
                ExpenseByCategoryList(expensesByCategory, total,
                    currentFilters = filters, onApply = { newFilters ->
                    isLoading = true
                    filters = newFilters
                })
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onClick: (id: Int) -> Unit) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RectangleShape,
        onClick = {
            onClick(expense.id)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
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
                expense.category?.let {
                    if (expense.category.id == 0) return@let

                    Box(
                        modifier = Modifier
                            .clip(
                                CircleShape
                            )
                            .size(48.dp)
                            .background(Color(expense.category.color.toColorInt())),
                        contentAlignment = Alignment.Center
                    ) {
                        SvgIcon(
                            iconUrl = expense.category.url,
                            label = expense.category.name,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
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
