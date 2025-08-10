package com.example.finad.ui.component.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormContainer(
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    headerSpacing: Int = 24,
    contentSpacing: Int = 20
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(headerSpacing.dp)
    ) {
        header()
        
        Column(
            verticalArrangement = Arrangement.spacedBy(contentSpacing.dp)
        ) {
            content()
        }
    }
}
