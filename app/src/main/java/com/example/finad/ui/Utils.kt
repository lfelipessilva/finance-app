package com.example.finad.ui

import android.icu.text.NumberFormat
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatToDate(iso: String): String {
    return try {
        ZonedDateTime.parse(iso).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: Exception) {
        iso
    }
}

fun safeColor(colorString: String?, fallback: Color = Color.Gray): Color {
    return try {
        if (!colorString.isNullOrBlank()) {
            Color(colorString.toColorInt())
        } else fallback
    } catch (e: Exception) {
        fallback
    }
}

fun formatToCurrency(valueInCents: Int): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return currencyFormatter.format(valueInCents / 100.0)
}
