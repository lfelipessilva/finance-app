package com.orbita.finad.data.remote

import com.orbita.finad.data.remote.dto.CreateExpenseDto
import com.orbita.finad.data.remote.dto.ListExpenseByCategoryResponseDto
import com.orbita.finad.data.remote.dto.ListExpenseFilterDto
import com.orbita.finad.data.remote.dto.ListExpenseResponseDto
import com.orbita.finad.data.remote.entity.Expense
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object ExpenseService {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun createExpense(expense: CreateExpenseDto, callback: (Boolean) -> Unit) {
        try {
            val adapter = moshi.adapter(CreateExpenseDto::class.java)
            val json = adapter.toJson(expense)

            ApiClient.post("/expenses", json) { success, _ -> callback(success) }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }
    }

    fun updateExpense(id: String, expense: CreateExpenseDto, callback: (Boolean) -> Unit) {
        try {
            val adapter = moshi.adapter(CreateExpenseDto::class.java)
            val json = adapter.toJson(expense)

            ApiClient.put("/expenses/$id", json) { success, _ -> callback(success) }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }
    }

    fun deleteExpense(id: String, callback: (Boolean) -> Unit) {
        ApiClient.delete("/expenses/$id") { success, _ -> callback(success) }
    }

    fun getExpenseById(id: String, callback: (Boolean, Expense?) -> Unit) {
        ApiClient.get("/expenses/$id") { success, response ->
            if (success && response != null) {
                try {
                    val adapter = moshi.adapter(Expense::class.java)
                    val expense = adapter.fromJson(response)
                    callback(true, expense)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(false, null)
                }
            } else {
                callback(false, null)
            }
        }
    }

    fun getAllExpenses(
            filters: ListExpenseFilterDto,
            callback: (Boolean, ListExpenseResponseDto?) -> Unit
    ) {
        try {
            val queryParams =
                    buildQueryParams(filters, listOf("order_by=timestamp", "order_direction=desc"))
            ApiClient.get("/expenses?$queryParams") { success, response ->
                if (success && response != null) {
                    try {
                        val adapter = moshi.adapter(ListExpenseResponseDto::class.java)
                        val body = adapter.fromJson(response)
                        callback(true, body)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(false, null)
                    }
                } else {
                    callback(false, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false, null)
        }
    }

    fun getAllExpensesByCategory(
            filters: ListExpenseFilterDto?,
            callback: (Boolean, ListExpenseByCategoryResponseDto?) -> Unit
    ) {
        val queryParams =
                buildQueryParams(filters, listOf("order_by=total_value", "order_direction=desc"))
        ApiClient.get("/expenses/category?$queryParams") { success, response ->
            if (success && response != null) {
                try {
                    val adapter = moshi.adapter(ListExpenseByCategoryResponseDto::class.java)
                    val body = adapter.fromJson(response)
                    callback(true, body)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(false, null)
                }
            } else {
                callback(false, null)
            }
        }
    }

    private fun buildQueryParams(
            filters: ListExpenseFilterDto?,
            start_filters: List<String>
    ): String {
        val params = start_filters.toMutableList()

        filters?.page.let { params.add("page=$it") }
        filters?.pageSize.let { params.add("page_size=$it") }
        filters?.category?.let { params.add("category=$it") }
        filters?.name?.let { params.add("name=$it") }
        filters?.timestampStart?.let {
            params.add(
                    "timestamp_start=${
                    convertMillisToIso8601WithTime(
                        it
                    )
                }"
            )
        }
        filters?.timestampEnd?.let {
            params.add("timestamp_end=${convertMillisToIso8601WithTime(it)}")
        }

        return params.joinToString("&")
    }

    fun convertMillisToIso8601WithTime(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(millis))
    }
}
