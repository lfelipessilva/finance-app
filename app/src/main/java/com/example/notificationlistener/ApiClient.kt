package com.example.notificationlistener

import com.example.notificationlistener.entity.Expense
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "https://xenacious-tern-luis-finance-e5c5636e.koyeb.app/api/v1"

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .callTimeout(5, TimeUnit.MINUTES)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request & response body
        })
        .build()

    // Function to send data to the server
    fun sendNotification(notificationData: Expense, callback: (Boolean) -> Unit) {
        val json = JSONObject().apply {
            put("name", notificationData.name)
            put("value", notificationData.value)
            put("card", notificationData.card)
            put("bank", notificationData.bank)
            put("categoryId", notificationData.categoryId)
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
