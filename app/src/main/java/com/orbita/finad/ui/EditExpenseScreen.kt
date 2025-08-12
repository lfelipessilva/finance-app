package com.orbita.finad.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orbita.finad.ui.component.CategorySelectionDialog
import com.orbita.finad.ui.component.form.CategorySelectionField
import com.orbita.finad.ui.component.form.FormContainer
import com.orbita.finad.ui.component.form.FormField
import com.orbita.finad.ui.component.form.FormFieldData
import com.orbita.finad.ui.component.form.FormFieldRow
import com.orbita.finad.views.ExpenseViewModel

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
        var description by remember { mutableStateOf(expense.description) }
        var value by remember { mutableStateOf(expense.value.toString()) }
        var bank by remember { mutableStateOf(expense.bank) }
        var card by remember { mutableStateOf(expense.card) }
        var selectedCategory by remember { mutableStateOf(expense.category) }
        var timestamp by remember { mutableStateOf(expense.timestamp) }
        var showCategoryDialog by remember { mutableStateOf(false) }
        var isSaving by remember { mutableStateOf(false) }

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

        Column(modifier = Modifier.fillMaxSize()) {
                CustomTopAppBar(
                        title = "Editar Despesa",
                        onBack = { navController.popBackStack() },
                        rightItem = {
                                FilledTonalButton(
                                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                                        onClick = {
                                                isSaving = true
                                                val updatedExpense =
                                                        expense.copy(
                                                                name = name,
                                                                description = description,
                                                                value = value.toIntOrNull()
                                                                                ?: expense.value,
                                                                bank = bank,
                                                                card = card,
                                                                categoryId = selectedCategory?.id,
                                                                category = selectedCategory,
                                                                timestamp = timestamp
                                                        )

                                                expenseViewModel.updateExpense(updatedExpense) {
                                                        success ->
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
                                                        !isSaving,
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

                FormContainer(
                        header = {},
                        content = {
                                FormField(
                                        label = "Nome",
                                        value = name,
                                        onValueChange = { name = it },
                                        placeholder = "Digite o nome da despesa",
                                        modifier = Modifier.fillMaxWidth()
                                )

                                FormField(
                                        label = "Descrição",
                                        value = description ?: "",
                                        onValueChange = { description = it },
                                        placeholder = "Digite a descrição",
                                        modifier = Modifier.fillMaxWidth()
                                )

                                FormField(
                                        label = "Valor",
                                        value = value,
                                        onValueChange = {
                                                if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                                                        value = it
                                                }
                                        },
                                        placeholder = "0",
                                        keyboardType = KeyboardType.Number,
                                        modifier = Modifier.fillMaxWidth(),
                                        trailingIcon = {
                                                if (value.isNotBlank()) {
                                                        Text(
                                                                formatToCurrency(value.toInt()),
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodyMedium,
                                                                color =
                                                                        MaterialTheme.colorScheme
                                                                                .primary,
                                                                fontWeight = FontWeight.Medium
                                                        )
                                                }
                                        }
                                )

                                FormFieldRow(
                                        fields =
                                                listOf(
                                                        FormFieldData(
                                                                label = "Banco",
                                                                value = bank,
                                                                onValueChange = { bank = it },
                                                                placeholder = "Banco"
                                                        ),
                                                        FormFieldData(
                                                                label = "Cartão *",
                                                                value = card,
                                                                onValueChange = {
                                                                        if (it.length <= 4) {
                                                                                card = it
                                                                        }
                                                                },
                                                                keyboardType = KeyboardType.Number,
                                                                placeholder = "0000"
                                                        )
                                                )
                                )

                                CategorySelectionField(
                                        label = "Categoria",
                                        selectedCategory = selectedCategory,
                                        onCategoryClick = { showCategoryDialog = true }
                                )

                                FormField(
                                        label = "Data",
                                        value = formatToDate(timestamp),
                                        onValueChange = {},
                                        placeholder = "Data da despesa",
                                        enabled = false,
                                        modifier = Modifier.fillMaxWidth(),
                                        trailingIcon = {
                                                Icon(
                                                        Icons.Default.Edit,
                                                        contentDescription = "Data fixa",
                                                        tint =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant.copy(
                                                                        alpha = 0.6f
                                                                )
                                                )
                                        }
                                )
                        }
                )
        }
}
