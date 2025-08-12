package com.orbita.finad.ui.component.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FormField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String? = null,
        keyboardType: KeyboardType = KeyboardType.Text,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        trailingIcon: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        isError: Boolean = false,
        errorMessage: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder?.let { { Text(it) } },
                modifier = modifier,
                singleLine = singleLine,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors =
                        OutlinedTextFieldDefaults.colors(
                                focusedBorderColor =
                                        if (isError) MaterialTheme.colorScheme.error
                                        else MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor =
                                        if (isError) MaterialTheme.colorScheme.error
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                disabledBorderColor =
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                disabledTextColor =
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                shape = RoundedCornerShape(16.dp),
                trailingIcon =
                        trailingIcon?.let { icon ->
                            { Box(modifier = Modifier.padding(8.dp)) { icon() } }
                        },
                supportingText = supportingText,
                isError = isError
        )

        if (isError && errorMessage != null) {
            Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun FormFieldRow(fields: List<FormFieldData>, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        fields.forEach { fieldData ->
            Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormField(
                        label = fieldData.label,
                        value = fieldData.value,
                        onValueChange = fieldData.onValueChange,
                        placeholder = fieldData.placeholder,
                        keyboardType = fieldData.keyboardType,
                        singleLine = fieldData.singleLine,
                        enabled = fieldData.enabled,
                        trailingIcon = fieldData.trailingIcon,
                        supportingText = fieldData.supportingText,
                        isError = fieldData.isError,
                        errorMessage = fieldData.errorMessage
                )
            }
        }
    }
}

data class FormFieldData(
        val label: String,
        val value: String,
        val onValueChange: (String) -> Unit,
        val placeholder: String? = null,
        val keyboardType: KeyboardType = KeyboardType.Text,
        val singleLine: Boolean = true,
        val enabled: Boolean = true,
        val trailingIcon: @Composable (() -> Unit)? = null,
        val supportingText: @Composable (() -> Unit)? = null,
        val isError: Boolean = false,
        val errorMessage: String? = null
)
