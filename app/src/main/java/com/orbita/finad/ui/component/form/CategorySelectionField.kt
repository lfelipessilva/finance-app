package com.orbita.finad.ui.component.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.orbita.finad.data.remote.entity.Category
import com.orbita.finad.ui.component.SvgIcon
import com.orbita.finad.ui.safeColor

@Composable
fun CategorySelectionField(
        label: String,
        selectedCategory: Category?,
        onCategoryClick: () -> Unit,
        modifier: Modifier = Modifier
) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                )

                Card(
                        modifier = modifier.fillMaxWidth().clickable { onCategoryClick() },
                        colors =
                                CardDefaults.cardColors(
                                        containerColor =
                                                MaterialTheme.colorScheme.surfaceVariant.copy(
                                                        alpha = 0.5f
                                                )
                                ),
                        shape = RoundedCornerShape(16.dp),
                        border =
                                BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                )
                ) {
                        Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                if (selectedCategory != null) {
                                        Box(
                                                modifier =
                                                        Modifier.size(48.dp)
                                                                .clip(RoundedCornerShape(12.dp))
                                                                .background(
                                                                        safeColor(
                                                                                selectedCategory
                                                                                        .color
                                                                        ),
                                                                        shape =
                                                                                RoundedCornerShape(
                                                                                        12.dp
                                                                                )
                                                                ),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                SvgIcon(
                                                        iconUrl = selectedCategory.url ?: "",
                                                        label = selectedCategory.name ?: "",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(28.dp)
                                                )
                                        }
                                        Column {
                                                Text(
                                                        text = selectedCategory.name ?: "",
                                                        style =
                                                                MaterialTheme.typography.bodyLarge
                                                                        .copy(
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .Medium
                                                                        )
                                                )
                                                Text(
                                                        text = "Toque para alterar",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant
                                                )
                                        }
                                } else {
                                        Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "Selecionar categoria",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                                text = "Selecionar categoria",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                }
                        }
                }
        }
}
