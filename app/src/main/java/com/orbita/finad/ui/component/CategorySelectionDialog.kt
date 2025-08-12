package com.orbita.finad.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.orbita.finad.data.remote.entity.Category
import com.orbita.finad.ui.safeColor

@Composable
fun CategorySelectionDialog(
        categories: List<Category>,
        onCategorySelected: (Category) -> Unit,
        onDismiss: () -> Unit
) {
        Dialog(onDismissRequest = onDismiss) {
                Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        shape = MaterialTheme.shapes.large,
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                )
                ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                        text = "Selecione",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                )

                                LazyColumn(modifier = Modifier.heightIn(max = 500.dp)) {
                                        items(categories) { category ->
                                                Row(
                                                        modifier =
                                                                Modifier.fillMaxWidth()
                                                                        .clickable {
                                                                                onCategorySelected(
                                                                                        category
                                                                                )
                                                                                onDismiss()
                                                                        }
                                                                        .padding(vertical = 8.dp),
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        Box(
                                                                modifier =
                                                                        Modifier.clip(CircleShape)
                                                                                .size(40.dp)
                                                                                .background(
                                                                                        safeColor(
                                                                                                category.color
                                                                                        )
                                                                                ),
                                                                contentAlignment = Alignment.Center
                                                        ) {
                                                                SvgIcon(
                                                                        iconUrl = category.url,
                                                                        label = category.name,
                                                                        tint = Color.White,
                                                                        modifier =
                                                                                Modifier.size(24.dp)
                                                                )
                                                        }

                                                        Spacer(modifier = Modifier.width(12.dp))

                                                        Text(
                                                                text = category.name,
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodyLarge,
                                                                color =
                                                                        MaterialTheme.colorScheme
                                                                                .onSurface
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}
