package com.example.finad.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finad.data.remote.entity.Tag
import androidx.compose.material3.CircularProgressIndicator
import com.example.finad.views.UpdateExpenseViewModel

@Composable
fun ExpenseDetailScreen(
    expenseId: String,
    viewModel: UpdateExpenseViewModel = viewModel(),
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }
    var card by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var selectedTags by remember { mutableStateOf<List<Tag>>(emptyList()) }

    val expense = viewModel.expense
    val isLoading = viewModel.isLoading
    val categories = viewModel.categories

    LaunchedEffect(expenseId) {
        viewModel.loadAll(expenseId)
    }

    if (expense != null && name.isBlank()) {
        name = expense.name
        value = expense.value.toString()
        bank = expense.bank
        card = expense.card
        selectedCategoryId = expense.categoryId
        selectedTags = expense.tags
    }

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        expense != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Edit Expense", style = MaterialTheme.typography.headlineMedium)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it.filter { it.isDigit() } },
                    label = { Text("Amount") },
                    leadingIcon = { Text("💰") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bank,
                    onValueChange = { bank = it },
                    label = { Text("Bank") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = card,
                    onValueChange = { card = it },
                    label = { Text("Card") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Category", style = MaterialTheme.typography.titleMedium)
                var categoryExpanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { categoryExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                        val selectedCategory = categories.find { it.id == selectedCategoryId }
                        Text(selectedCategory?.name ?: "Select Category")
                    }
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    categoryExpanded = false
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                selectedCategoryId = null
                                categoryExpanded = false
                            }
                        )
                    }
                }

//                Text("Tags", style = MaterialTheme.typography.titleMedium)
//                FlowRow(
//                    mainAxisSpacing = 8.dp,
//                    crossAxisSpacing = 8.dp
//                ) {
//                    tags.forEach { tag ->
//                        FilterChip(
//                            selected = selectedTags.contains(tag),
//                            onClick = {
//                                selectedTags = if (selectedTags.contains(tag)) {
//                                    selectedTags - tag
//                                } else {
//                                    selectedTags + tag
//                                }
//                            },
//                            label = { Text(tag.name) }
//                        )
//                    }
//                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val updatedExpense = expense.copy(
                            name = name,
                            value = value.toIntOrNull() ?: expense.value,
                            bank = bank,
                            card = card,
                            categoryId = selectedCategoryId,
                            tags = selectedTags
                        )
                        viewModel.updateExpense(expenseId, updatedExpense) {
                            onBack()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
