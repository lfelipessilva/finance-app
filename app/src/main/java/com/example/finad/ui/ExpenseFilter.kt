package com.example.finad.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finad.data.remote.dto.ListExpenseFilterDto
import com.example.finad.ui.component.DatePickerFieldToModal
import com.example.finad.views.ExpenseViewModel
import kotlinx.coroutines.launch
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFilterScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel,
) {
    var category by remember { mutableStateOf(expenseViewModel.filters.category ?: "")}
    var name by remember { mutableStateOf(expenseViewModel.filters.name?: "") }
    var timestampStart by remember { mutableStateOf(expenseViewModel.filters.timestampStart) }
    var timestampEnd by remember { mutableStateOf(expenseViewModel.filters.timestampEnd)}

    Scaffold (
        topBar = {
            CustomTopAppBar(title = "Filtros", onBack = { navController.navigate("expense/list")})
        },
    ){ innerPadding ->
        Column(Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerFieldToModal(
                value = timestampStart,
                onValueChange = { timestampStart = it },
                label = "Start date",
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerFieldToModal(
                value = timestampEnd,
                onValueChange = { timestampEnd = it },
                label = "End date",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    expenseViewModel.updateFilters(
                        ListExpenseFilterDto(
                            category = category.ifBlank { null },
                            name = name.ifBlank { null },
                            timestampStart = timestampStart,
                            timestampEnd = timestampEnd
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filters")
            }

            Button(
                onClick = {
                    expenseViewModel.updateFilters(ListExpenseFilterDto())
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Reset Filters")
            }
        }
    }
}
