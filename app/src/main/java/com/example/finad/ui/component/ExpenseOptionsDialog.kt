package com.example.finad.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ExpenseOptionsDialog(onDismiss: () -> Unit, onDelete: () -> Unit) {
        var showConfirmation by remember { mutableStateOf(false) }

        if (showConfirmation) {
                Dialog(onDismissRequest = { showConfirmation = false }) {
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
                                                text = "Tem certeza?",
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                                text = "Esta ação não pode ser desfeita.",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                        ) {
                                                TextButton(onClick = { showConfirmation = false }) {
                                                        Text("Cancelar")
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Button(
                                                        onClick = {
                                                                onDelete()
                                                                onDismiss()
                                                        },
                                                        colors =
                                                                ButtonDefaults.buttonColors(
                                                                        containerColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .error
                                                                )
                                                ) { Text("Excluir") }
                                        }
                                }
                        }
                }
        } else {
                Dialog(onDismissRequest = onDismiss) {
                        Card(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                shape = MaterialTheme.shapes.large,
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                        )
                        ) {
                                Row(
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .clickable { showConfirmation = true }
                                                        .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Text(
                                                text = "Excluir",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.error
                                        )
                                }
                        }
                }
        }
}
