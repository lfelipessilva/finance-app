package com.example.notificationlistener.data.remote

import com.example.notificationlistener.data.remote.dto.CreateExpenseDto
import com.example.notificationlistener.data.remote.dto.ListExpenseFilterDto
import com.example.notificationlistener.data.remote.dto.ListExpenseResponseDto
import com.example.notificationlistener.data.remote.entity.Category
import com.example.notificationlistener.data.remote.entity.Expense
import com.example.notificationlistener.data.remote.entity.Tag
import com.squareup.moshi.Moshi
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object ExpenseService {

    fun createExpense(expense: CreateExpenseDto, callback: (Boolean) -> Unit) {
        val json = toJson(expense)
        ApiClient.post("/expenses", json) { success, _ ->
            callback(success)
        }
    }

    fun updateExpense(id: String, expense: CreateExpenseDto, callback: (Boolean) -> Unit) {
        val json = toJson(expense)
        ApiClient.put("/expenses/$id", json) { success, _ ->
            callback(success)
        }
    }

    fun deleteExpense(id: String, callback: (Boolean) -> Unit) {
        ApiClient.delete("/expenses/$id") { success, _ ->
            callback(success)
        }
    }

    fun getExpenseById(id: String, callback: (Boolean, Expense?) -> Unit) {
        ApiClient.get("/expenses/$id") { success, response ->
            if (success && response != null) {
                val json = JSONObject(response)
                val expense = fromJson(json)
                callback(true, expense)
            } else {
                callback(false, null)
            }
        }
    }

    fun getAllExpenses(
        filters: ListExpenseFilterDto,
        callback: (Boolean, ListExpenseResponseDto?) -> Unit
    ) {
        val queryParams = buildQueryParams(filters)
        ApiClient.get("/expenses?$queryParams") { success, response ->
            if (success && response != null) {
                try {
                    val moshi = Moshi.Builder().build()
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
    }

    private fun buildQueryParams(filters: ListExpenseFilterDto): String {
        val params = mutableListOf("order_by=timestamp", "order_direction=desc")

        filters.page.let { params.add("page=$it") }
        filters.pageSize.let { params.add("page_size=$it") }
        filters.category?.let { params.add("category=$it") }
        filters.name?.let { params.add("name=$it") }
        filters.timestampStart?.let {
            params.add(
                "timestamp_start=${
                    convertMillisToIso8601WithTime(
                        it
                    )
                }"
            )
        }
        filters.timestampEnd?.let { params.add("timestamp_end=${convertMillisToIso8601WithTime(it)}") }

        return params.joinToString("&")
    }

    fun convertMillisToIso8601WithTime(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(millis))
    }

    private fun toJson(expense: CreateExpenseDto): JSONObject {
        return JSONObject().apply {
            put("name", expense.name)
            put("value", expense.value)
            put("card", expense.card)
            put("bank", expense.bank)
            put("timestamp", expense.timestamp)
        }
    }

    private fun fromJson(json: JSONObject): Expense {
        val categoryJson = json.optJSONObject("category")
        val category = if (categoryJson != null) {
            Category(
                id = categoryJson.optInt("id"),
                name = categoryJson.optString("name"),
                color = categoryJson.optString("color"),
                icon = categoryJson.optString("icon"),
                url = categoryJson.optString("icon")
            )
        } else {
            null
        }

        val tagsJsonArray = json.optJSONArray("tags")
        val tags = mutableListOf<Tag>()
        if (tagsJsonArray != null) {
            for (i in 0 until tagsJsonArray.length()) {
                val tagJson = tagsJsonArray.optJSONObject(i)
                if (tagJson != null) {
                    tags.add(
                        Tag(
                            id = tagJson.optInt("id"),
                            name = tagJson.optString("name"),
                            color = tagJson.optString("color")
                        )
                    )
                }
            }
        }

        return Expense(
            id = json.optInt("id"),
            name = json.optString("name"),
            value = json.optInt("value"),
            card = json.optString("card"),
            bank = json.optString("bank"),
            timestamp = json.optString("timestamp"),
            categoryId = json.optInt("category_id"),
            category = category,
            tags = tags
        )
    }
}
