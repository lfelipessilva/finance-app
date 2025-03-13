package com.example.notificationlistener

import com.example.notificationlistener.entity.Expense
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException

object ApiClient {

    private const val BASE_URL = "https://finance-server-vr5e.onrender.com/api/v1"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request & response body
        })
        .build()

    // Function to send data to the server
    fun sendNotification(notificationData: Expense, callback: (Boolean) -> Unit) {
        val json = JSONObject().apply {
            put("name", notificationData.name)
            put("value", notificationData.value)
            put("category", notificationData.category)
            put("timestamp", notificationData.timestamp)
        }

        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("$BASE_URL/expenses")
            .post(requestBody)
            .build()

        // Asynchronous request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
        })
    }
}
