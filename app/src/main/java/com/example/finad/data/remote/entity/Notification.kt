package com.example.finad.data.remote.entity

import android.content.Context
import android.util.Log
import com.example.finad.data.AppDatabase
import com.example.finad.data.local.entity.Expense as LocalExpense
import com.example.finad.data.remote.ExpenseService
import com.example.finad.data.remote.dto.CreateExpenseDto
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BankNotification(
        private val text: String,
        private val context: Context,
        var success: Boolean = false,
        val timestamp: String =
                Instant.now()
                        .atOffset(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
        val bank: String,
) {
    fun extractValue(): Int {
        val regex = """R\$\s*([\d,.]+)""".toRegex()
        val match = regex.find(this.text)
        val value =
                match?.groupValues?.get(1)?.replace(",", ".")?.toDoubleOrNull()?.times(100)?.toInt()
                        ?: 0
        Log.d("BankNotification", "Extracted value: $value from text: $text")
        return value
    }

    fun extractName(): String {
        val regex: Regex =
                when (this.bank.lowercase()) {
                    "inter" -> """em\s([A-Za-z0-9* ]+?)\.""".toRegex()
                    "nubank" -> """em\s([A-Za-z0-9* ]+?)\spara\s""".toRegex()
                    "santander" -> """em\s([A-Za-z0-9* ]+?),\saprovada""".toRegex()
                    else -> return "Unknown"
                }

        val match = regex.find(this.text)
        val name = match?.groupValues?.get(1)?.trim() ?: "Unknown"
        Log.d("BankNotification", "Extracted name: $name from text: $text")
        return name
    }

    fun extractCard(): String {
        val regex = """final\s(\d{4})""".toRegex() // Matches "final 3455"
        val card = regex.find(this.text)?.groupValues?.get(1) ?: "Unknown"
        Log.d("BankNotification", "Extracted card: $card from text: $text")
        return card
    }

    fun sendToServer() {
        Log.d("BankNotification", "Sending to server: bank=$bank, text=$text")
        val dto =
                CreateExpenseDto(
                        name = this.extractName(),
                        value = this.extractValue(),
                        bank = this.bank,
                        card = this.extractCard(),
                        timestamp = this.timestamp,
                        categoryId = 1
                )

        ExpenseService.createExpense(dto) { success ->
            this.success = success
            Log.d("BankNotification", "Server response: success=$success")
            this.saveToDatabase()
        }
    }

    private fun saveToDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getDatabase(context)
                val expense =
                        LocalExpense(
                                name = this@BankNotification.extractName(),
                                value = this@BankNotification.extractValue(),
                                bank = this@BankNotification.bank,
                                card = this@BankNotification.extractCard(),
                                timestamp = this@BankNotification.timestamp,
                                success = this@BankNotification.success
                        )

                db.expenseDao().insertExpense(expense)
                Log.d("BankNotification", "Saved to database: $expense")
            } catch (e: Exception) {
                Log.e("BankNotification", "Error saving to database", e)
            }
        }
    }
}
