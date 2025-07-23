package com.example.finad.data.remote

import com.example.finad.data.remote.dto.CreateExpenseDto
import com.example.finad.data.remote.dto.ListExpenseByCategoryResponseDto
import com.example.finad.data.remote.dto.ListExpenseFilterDto
import com.example.finad.data.remote.dto.ListExpenseResponseDto
import com.example.finad.data.remote.entity.Category
import com.example.finad.data.remote.entity.Expense
import com.example.finad.data.remote.entity.Tag
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class TokenHolder(
    val token: String
)

object UserService {
    fun createUser(token: String, callback: (Boolean) -> Unit) {
        val body = JSONObject().apply {
            put("token", token)
        }

        ApiClient.post("/user", jsonBody = body ) { success, _ ->
            callback(success)
        }
    }
}
