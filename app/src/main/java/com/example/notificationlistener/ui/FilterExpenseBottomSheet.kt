import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterExpenseBottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (openSheet: () -> Unit) -> Unit
) {
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold (
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
