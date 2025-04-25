package com.example.finad.ui.component

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.finad.data.remote.dto.ListExpenseFilterDto
import com.example.finad.data.remote.entity.ExpenseByCategory
import com.example.finad.ui.formatToCurrency

@Composable
fun ExpenseByCategoryList(
    expensesByCategory: List<ExpenseByCategory>,
    total: Int,
    currentFilters: ListExpenseFilterDto,
    onApply: (ListExpenseFilterDto) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
        Text(
            text = formatToCurrency(total),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        StackedBar(expensesByCategory)

        Column {
            expensesByCategory.forEach { category ->
                val isSelected = currentFilters.category === category.category_id.toString()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.background else Color.Transparent)
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                        .clickable() {
                            onApply(
                                currentFilters.copy(
                                    category = if (isSelected) null else category.category_id.toString(),
                                )
                            )
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(category.category_color.toColorInt())),
                        )

                        Text(text = category.category_name, fontSize = 14.sp)
                    }

                    Text(
                        text = formatToCurrency(category.total_value),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

