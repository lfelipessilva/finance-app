package com.example.finad.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.finad.data.remote.entity.Expense
import com.example.finad.ui.component.CategorySelectionDialog
import com.example.finad.ui.component.ExpenseByCategoryList
import com.example.finad.ui.component.SvgIcon
import com.example.finad.views.ExpenseViewModel
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(navController: NavController, expenseViewModel: ExpenseViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        expenseViewModel.fetchExpenses()
        expenseViewModel.fetchExpensesByCategory()
        expenseViewModel.fetchAllCategories()
    }

    LaunchedEffect(expenseViewModel.endReached, expenseViewModel.isFetchingMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.collect { lastVisibleItem ->
            if (lastVisibleItem != null &&
                lastVisibleItem >= expenseViewModel.expenses.lastIndex &&
                !expenseViewModel.endReached &&
                !expenseViewModel.isFetchingMore &&
                expenseViewModel.expenses.isNotEmpty()
            ) {
                expenseViewModel.fetchMoreExpenses()
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight =
            (screenHeight - (142.dp + (expenseViewModel.expensesByCategory.size * 28).dp)),
        sheetContainerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CustomTopAppBar(
                title = "Olá, Luís",
                onSearch = { navController.navigate("expense/filter") },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            )
        },
        sheetContent = {
            if (expenseViewModel.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                ) {
                    itemsIndexed(expenseViewModel.expenses) { index, expense ->
                        ExpenseItem(expense, expenseViewModel)
                    }

                    if (!expenseViewModel.endReached && expenseViewModel.expenses.isNotEmpty()
                    ) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator(Modifier.padding(16.dp)) }
                        }
                    }
                }
            }
        }
    ) {
        ExpenseByCategoryList(
            expenseViewModel.expensesByCategory,
            expenseViewModel.total,
            currentFilters = expenseViewModel.filters,
            onApply = { newFilters -> expenseViewModel.updateFilters(newFilters) }
        )
    }
}

@Composable
fun ExpenseItem(expense: Expense, expenseViewModel: ExpenseViewModel) {
    var showCategoryDialog by remember { mutableStateOf(false) }

    if (showCategoryDialog) {
        CategorySelectionDialog(
            categories = expenseViewModel.categories,
            onCategorySelected = { category ->
                val updatedExpense = expense.copy(categoryId = category.id, category = category)
                expenseViewModel.updateExpense(updatedExpense) {}
            },
            onDismiss = { showCategoryDialog = false }
        )
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
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
                Box(
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .size(48.dp)
                            .background(safeColor(expense.category?.color))
                            .clickable { showCategoryDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (expense.category != null) {
                        SvgIcon(
                            iconUrl = expense.category.url,
                            label = expense.category.name,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Text(
                            text = "?",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
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

fun safeColor(colorString: String?, fallback: Color = Color.Gray): Color {
    return try {
        if (!colorString.isNullOrBlank()) {
            Color(colorString.toColorInt())
        } else fallback
    } catch (e: Exception) {
        fallback
    }
}
