package com.example.notificationlistener.data.remote

import com.example.notificationlistener.data.remote.entity.Expense
import org.json.JSONObject
import org.json.JSONArray

object ExpenseService {

    fun createExpense(expense: Expense, callback: (Boolean) -> Unit) {
        val json = toJson(expense)
        ApiClient.post("/expenses", json) { success, _ ->
            callback(success)
        }
    }

    fun updateExpense(id: String, expense: Expense, callback: (Boolean) -> Unit) {
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
        ApiClient.get("/expenses") { success, response ->
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

    private fun toJson(expense: Expense): JSONObject {
        return JSONObject().apply {
            put("name", expense.name)
            put("value", expense.value)
            put("card", expense.card)
            put("bank", expense.bank)
            put("timestamp", expense.timestamp)
        }
    }

    private fun fromJson(json: JSONObject): Expense {
        return Expense(
            name = json.optString("name"),
            value = json.optInt("value"),
            card = json.optString("card"),
            bank = json.optString("bank"),
            timestamp = json.optString("timestamp"),
            categoryId = json.optInt("category_id")
        )
    }
}
