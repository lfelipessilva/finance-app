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
import com.orbita.finad.data.remote.dto.CreateExpenseDto
import com.orbita.finad.data.remote.entity.Category
import com.orbita.finad.ui.component.CategorySelectionDialog
import com.orbita.finad.ui.component.form.CategorySelectionField
import com.orbita.finad.ui.component.form.FormContainer
import com.orbita.finad.ui.component.form.FormField
import com.orbita.finad.ui.component.form.FormFieldData
import com.orbita.finad.ui.component.form.FormFieldRow
import com.orbita.finad.views.ExpenseViewModel
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExpenseScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel,
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }
    var card by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var timestamp by remember {
        mutableStateOf(
            Instant.now()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
        )
    }
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
            title = "Nova Despesa",
            onBack = { navController.popBackStack() },
            rightItem = {
                FilledTonalButton(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    onClick = {
                        val isFormValid =
                            name.isNotBlank() &&
                                    value.isNotBlank() &&
                                    bank.isNotBlank() &&
                                    card.isNotBlank()

                        if (isFormValid) {
                            isSaving = true
                            val expense =
                                CreateExpenseDto(
                                    name = name.trim(),
                                    description =
                                        description.trim()
                                            .takeIf {
                                                it.isNotBlank()
                                            }
                                            ?: "",
                                    value = value.toIntOrNull()
                                        ?: 0,
                                    bank = bank.trim(),
                                    card = card.trim(),
                                    categoryId =
                                        selectedCategory
                                            ?.id,
                                    timestamp = timestamp
                                )

                            expenseViewModel.createExpense(expense) { success ->
                                isSaving = false
                                if (success) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    },
                    enabled =
                        (name.isNotBlank() &&
                                value.isNotBlank() &&
                                bank.isNotBlank() &&
                                card.isNotBlank()) && !isSaving,
                    colors =
                        ButtonDefaults.filledTonalButtonColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary
                        )
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
                    label = "Nome *",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Digite o nome da despesa",
                    modifier = Modifier.fillMaxWidth()
                )

                FormField(
                    label = "Descrição",
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Digite a descrição (opcional)",
                    modifier = Modifier.fillMaxWidth()
                )

                FormField(
                    label = "Valor *",
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
                                formatToCurrency(
                                    value.toIntOrNull() ?: 0
                                ),
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
                                label = "Banco *",
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
