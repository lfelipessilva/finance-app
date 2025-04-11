import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notificationlistener.data.remote.dto.ListExpenseFilterDto
import com.example.notificationlistener.ui.component.DatePickerFieldToModal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterExpenseBottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (openSheet: () -> Unit) -> Unit
) {
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = sheetContent,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        content {
            coroutineScope.launch {
                sheetState.bottomSheetState.expand()
            }
        }
    }
}

@Composable
fun ExpenseFilterSheetContent(
    initialFilters: ListExpenseFilterDto,
    onApply: (ListExpenseFilterDto) -> Unit
) {
    var category by remember { mutableStateOf(initialFilters.category ?: "") }
    var name by remember { mutableStateOf(initialFilters.name ?: "") }
    var timestampStart by remember { mutableStateOf(initialFilters.timestampStart) }
    var timestampEnd by remember { mutableStateOf(initialFilters.timestampEnd)}

    Column(Modifier.padding(16.dp)) {
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
                onApply(
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
                // Reset UI state
                category = ""
                name = ""
                timestampStart = null
                timestampEnd = null

                // Trigger filter with cleared values
                onApply(ListExpenseFilterDto())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Reset Filters")
        }
    }
}
