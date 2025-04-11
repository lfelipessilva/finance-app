package com.example.notificationlistener.data.remote.entity

import android.util.Log
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class BankNotification(
    private val text: String,
    val timestamp: String = Instant.now().atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
    val bank: String,
) {
    fun extractValue(): Int {
        val regex = """R\$\s*([\d,.]+)""".toRegex()
        val match = regex.find(this.text)
        return match?.groupValues?.get(1)
            ?.replace(",", ".")
            ?.toDoubleOrNull()
            ?.times(100)
            ?.toInt() ?: 0
    }

    fun extractName(): String {
        val regex: Regex = when (this.bank.lowercase()) {
            "inter" -> """em\s([A-Za-z0-9* ]+?)\.""".toRegex() // Stops at a literal "."
            "nubank" -> """em\s([A-Za-z0-9* ]+?)\spara\s""".toRegex() // Stops at "para"
            else -> return "Unknown"
        }

        val match = regex.find(this.text)
        return match?.groupValues?.get(1)?.trim() ?: "Unknown"
    }

    fun extractCard(): String {
        val regex = """final\s(\d{4})""".toRegex() // Matches "final 3455"
        return regex.find(this.text)?.groupValues?.get(1) ?: "Unknown"
    }
}
