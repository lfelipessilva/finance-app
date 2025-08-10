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
fun DeleteConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
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
                                        TextButton(onClick = onDismiss) { Text("Cancelar") }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                                onClick = onConfirm,
                                                colors =
                                                        ButtonDefaults.buttonColors(
                                                                containerColor =
                                                                        MaterialTheme.colorScheme
                                                                                .error
                                                        )
                                        ) { Text("Excluir") }
                                }
                        }
                }
        }
}

@Composable
fun ExpenseOptionsDialog(onDismiss: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
        var showConfirmation by remember { mutableStateOf(false) }

        if (showConfirmation) {
                DeleteConfirmationDialog(
                        onDismiss = { showConfirmation = false },
                        onConfirm = {
                                onDelete()
                                onDismiss()
                        }
                )
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
                                Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .clickable {
                                                                        onEdit()
                                                                        onDismiss()
                                                                }
                                                                .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                Text(
                                                        text = "Editar",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                )
                                        }

                                        Row(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .clickable {
                                                                        showConfirmation = true
                                                                }
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
}
