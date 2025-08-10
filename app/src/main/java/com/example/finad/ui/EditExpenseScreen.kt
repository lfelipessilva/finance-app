package com.example.finad.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finad.ui.component.CategorySelectionDialog
import com.example.finad.ui.component.SvgIcon
import com.example.finad.views.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
        navController: NavController,
        expenseViewModel: ExpenseViewModel,
        expenseId: Int
) {
        val expense = expenseViewModel.expenses.find { it.id == expenseId }

        if (expense == null) {
                LaunchedEffect(Unit) { navController.popBackStack() }
                return
        }

        var name by remember { mutableStateOf(expense.name) }
        var value by remember { mutableStateOf(expense.value.toString()) }
        var bank by remember { mutableStateOf(expense.bank) }
        var card by remember { mutableStateOf(expense.card) }
        var selectedCategory by remember { mutableStateOf(expense.category) }
        var timestamp by remember { mutableStateOf(expense.timestamp) }
        var showCategoryDialog by remember { mutableStateOf(false) }
        var showDatePicker by remember { mutableStateOf(false) }
        var isSaving by remember { mutableStateOf(false) }

        val scrollState = rememberScrollState()

        LaunchedEffect(Unit) { expenseViewModel.fetchAllCategories() }

        if (showCategoryDialog) {
                CategorySelectionDialog(
                        categories = expenseViewModel.categories,
                        onCategorySelected = { category ->
                                selectedCategory = category
                                showCategoryDialog = false
                        },
                        onDismiss = { showCategoryDialog = false }
                )
        }

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = { Text("Editar Despesa") },
                                navigationIcon = {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                                Icon(
                                                        Icons.Default.ArrowBack,
                                                        contentDescription = "Voltar"
                                                )
                                        }
                                },
                                actions = {
                                        TextButton(
                                                onClick = {
                                                        isSaving = true
                                                        val updatedExpense =
                                                                expense.copy(
                                                                        name = name,
                                                                        value = value.toIntOrNull()
                                                                                        ?: expense.value,
                                                                        bank = bank,
                                                                        card = card,
                                                                        categoryId =
                                                                                selectedCategory
                                                                                        ?.id,
                                                                        category = selectedCategory,
                                                                        timestamp = timestamp
                                                                )

                                                        expenseViewModel.updateExpense(
                                                                updatedExpense
                                                        ) { success ->
                                                                isSaving = false
                                                                if (success) {
                                                                        navController.popBackStack()
                                                                }
                                                        }
                                                },
                                                enabled =
                                                        name.isNotBlank() &&
                                                                value.isNotBlank() &&
                                                                bank.isNotBlank() &&
                                                                card.isNotBlank() &&
                                                                !isSaving
                                        ) {
                                                if (isSaving) {
                                                        CircularProgressIndicator(
                                                                modifier = Modifier.size(16.dp),
                                                                strokeWidth = 2.dp
                                                        )
                                                } else {
                                                        Text("Salvar")
                                                }
                                        }
                                }
                        )
                }
        ) { paddingValues ->
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(paddingValues)
                                        .verticalScroll(scrollState)
                                        .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Nome da despesa") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                        )

                        OutlinedTextField(
                                value = value,
                                onValueChange = {
                                        if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                                                value = it
                                        }
                                },
                                label = { Text("Valor (em centavos)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions =
                                        KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                supportingText = {
                                        if (value.isNotBlank()) {
                                                val valueInCents = value.toIntOrNull() ?: 0
                                                Text(
                                                        "R$ ${String.format("%.2f", valueInCents / 100.0)}"
                                                )
                                        }
                                }
                        )

                        OutlinedTextField(
                                value = bank,
                                onValueChange = { bank = it },
                                label = { Text("Banco") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                        )

                        OutlinedTextField(
                                value = card,
                                onValueChange = { card = it },
                                label = { Text("Cartão") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                        )

                        Card(
                                modifier =
                                        Modifier.fillMaxWidth().clickable {
                                                showCategoryDialog = true
                                        },
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.surfaceVariant
                                        )
                        ) {
                                Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                        if (selectedCategory != null) {
                                                Box(
                                                        modifier =
                                                                Modifier.size(40.dp)
                                                                        .background(
                                                                                safeColor(
                                                                                        selectedCategory
                                                                                                ?.color
                                                                                ),
                                                                                shape =
                                                                                        MaterialTheme
                                                                                                .shapes
                                                                                                .small
                                                                        ),
                                                        contentAlignment = Alignment.Center
                                                ) {
                                                        SvgIcon(
                                                                iconUrl = selectedCategory?.url
                                                                                ?: "",
                                                                label = selectedCategory?.name
                                                                                ?: "",
                                                                tint = Color.White,
                                                                modifier = Modifier.size(24.dp)
                                                        )
                                                }
                                                Text(
                                                        text = selectedCategory?.name ?: "",
                                                        style = MaterialTheme.typography.bodyLarge
                                                )
                                        } else {
                                                Icon(
                                                        imageVector = Icons.Default.Info,
                                                        contentDescription = "Selecionar categoria",
                                                        tint =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant
                                                )
                                                Text(
                                                        text = "Selecionar categoria",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant
                                                )
                                        }
                                }
                        }

                        OutlinedTextField(
                                value = formatToDate(timestamp),
                                onValueChange = {},
                                label = { Text("Data") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false,
                                singleLine = true
                        )
                }
        }
}
