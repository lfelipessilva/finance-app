package com.example.notificationlistener.data.remote

import com.example.notificationlistener.data.remote.dto.CreateExpenseDto
import com.example.notificationlistener.data.remote.entity.Category
import com.example.notificationlistener.data.remote.entity.Expense
import com.example.notificationlistener.data.remote.entity.Tag
import org.json.JSONObject
import org.json.JSONArray

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

    fun getAllExpenses(callback: (Boolean, List<Expense>) -> Unit) {
        ApiClient.get("/expenses?order_by=timestamp&order_direction=desc") { success, response ->
            if (success && response != null) {
                try {
                    val root = JSONObject(response)
                    val dataArray = root.getJSONArray("data")

                    val expenses = mutableListOf<Expense>()
                    for (i in 0 until dataArray.length()) {
                        val item = dataArray.getJSONObject(i)
                        expenses.add(fromJson(item))
                    }

                    callback(true, expenses)

                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(false, emptyList())
                }
            } else {
                callback(false, emptyList())
            }
        }
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
                    icon = categoryJson.optString("icon")
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
