package com.example.finad.ui

import androidx.compose.foundation.background
import com.example.finad.data.remote.ExpenseService
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finad.data.remote.dto.ListExpenseFilterDto
import com.example.finad.data.remote.entity.Expense
import com.example.finad.data.remote.entity.ExpenseByCategory
import com.example.finad.ui.component.ExpenseByCategoryList
import com.example.finad.ui.component.SvgIcon
import com.example.finad.views.ExpenseViewModel
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val listState = rememberLazyListState()

    var expenses = expenseViewModel.expenses
    var expensesByCategory = expenseViewModel.expensesByCategory
    var total = expenseViewModel.total
    var isLoading = expenseViewModel.isLoading
    var isFetchingMore = expenseViewModel.isFetchingMore
    var endReached = expenseViewModel.endReached
    var filters = expenseViewModel.filters

    LaunchedEffect(Unit) {
        expenseViewModel.fetchExpenses()
        expenseViewModel.fetchExpensesByCategory()
    }

    LaunchedEffect(listState, expenses.size, endReached, isFetchingMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItem ->
                if (
                    lastVisibleItem != null &&
                    lastVisibleItem >= expenses.lastIndex &&
                    !endReached &&
                    !isFetchingMore
                ) {
                    expenseViewModel.fetchMoreExpenses()
                }
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = (screenHeight - (142.dp + (expensesByCategory.size * 28).dp)),
        sheetContainerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CustomTopAppBar(
                title = "Olá, Luís",
                onSearch = { navController.navigate("expense/filter") },
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.surface
                )
            )
        },
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
                        ExpenseItem(expense)
                    }

                    if (!endReached && expenses.isNotEmpty()) {
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
        ExpenseByCategoryList(
            expensesByCategory, total,
            currentFilters = filters,
            onApply = { newFilters ->
                isLoading = true
                expenseViewModel.updateFilters(newFilters)
            }
        )
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RectangleShape,
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
