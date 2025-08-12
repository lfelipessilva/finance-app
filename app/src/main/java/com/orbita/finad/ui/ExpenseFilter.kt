package com.orbita.finad.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orbita.finad.data.remote.dto.ListExpenseFilterDto
import com.orbita.finad.ui.component.DatePickerFieldToModal
import com.orbita.finad.views.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFilterScreen(
        navController: NavController,
        expenseViewModel: ExpenseViewModel,
) {
        var category by remember { mutableStateOf(expenseViewModel.filters.category ?: "") }
        var name by remember { mutableStateOf(expenseViewModel.filters.name ?: "") }
        var timestampStart by remember { mutableStateOf(expenseViewModel.filters.timestampStart) }
        var timestampEnd by remember { mutableStateOf(expenseViewModel.filters.timestampEnd) }

        Scaffold(
                topBar = {
                        CustomTopAppBar(
                                title = "Filtros",
                                onBack = { navController.navigate("expense/list") }
                        )
                },
        ) { innerPadding ->
                Column(
                        Modifier.padding(innerPadding)
                                .padding(horizontal = 16.dp, vertical = 24.dp)
                                .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(
                                text = "Filtrar Gastos",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                                Column(Modifier.padding(16.dp)) {
                                        OutlinedTextField(
                                                value = name,
                                                onValueChange = { name = it },
                                                label = { Text("Nome") },
                                                modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        DatePickerFieldToModal(
                                                value = timestampStart,
                                                onValueChange = { timestampStart = it },
                                                label = "Data inicial",
                                                modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        DatePickerFieldToModal(
                                                value = timestampEnd,
                                                onValueChange = { timestampEnd = it },
                                                label = "Data final",
                                                modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Button(
                                                onClick = {
                                                        expenseViewModel.updateFilters(
                                                                ListExpenseFilterDto(
                                                                        category =
                                                                                category.ifBlank {
                                                                                        null
                                                                                },
                                                                        name =
                                                                                name.ifBlank {
                                                                                        null
                                                                                },
                                                                        timestampStart =
                                                                                timestampStart,
                                                                        timestampEnd = timestampEnd
                                                                )
                                                        )
                                                        navController.popBackStack(
                                                                "expense/list",
                                                                inclusive = false
                                                        )
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor =
                                                                        MaterialTheme.colorScheme
                                                                                .primary
                                                        )
                                        ) {
                                                Icon(
                                                        Icons.Default.Done,
                                                        contentDescription = "Aplicar"
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Text("Aplicar Filtros")
                                        }

                                        OutlinedButton(
                                                onClick = {
                                                        expenseViewModel.updateFilters(
                                                                ListExpenseFilterDto()
                                                        )
                                                },
                                                modifier =
                                                        Modifier.fillMaxWidth().padding(top = 8.dp)
                                        ) {
                                                Icon(
                                                        Icons.Default.Notifications,
                                                        contentDescription = "Resetar"
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Text("Resetar Filtros")
                                        }
                                }
                        }
                }
        }
}
