package com.example.finad.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.finad.data.remote.entity.ExpenseByCategory

@Composable
fun StackedBar(items: List<ExpenseByCategory>, modifier: Modifier = Modifier) {
    val total = items.sumOf { it.total_value }

    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        items.forEach { item ->
            val weight = if (total > 0) item.total_value.toFloat() / total.toFloat() else 0f

            Box(
                    modifier =
                            Modifier.fillMaxHeight()
                                    .weight(weight)
                                    .background(Color(item.category_color.toColorInt()))
            )
        }
    }
}
