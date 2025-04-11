package com.example.notificationlistener.data.remote

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
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    /**
     * Generic POST request
     */
    fun post(
        endpoint: String,
        jsonBody: JSONObject,
        callback: (Boolean, String?) -> Unit
    ) {
        val requestBody = jsonBody
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("$BASE_URL$endpoint")
            .post(requestBody)
            .build()

        executeRequest(request, callback)
    }

    /**
     * Generic GET request
     */
    fun get(
        endpoint: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val request = Request.Builder()
            .url("$BASE_URL$endpoint")
            .get()
            .build()

        executeRequest(request, callback)
    }

    /**
     * Generic PUT request
     */
    fun put(
        endpoint: String,
        jsonBody: JSONObject,
        callback: (Boolean, String?) -> Unit
    ) {
        val requestBody = jsonBody
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("$BASE_URL$endpoint")
            .put(requestBody)
            .build()

        executeRequest(request, callback)
    }

    /**
     * Generic DELETE request
     */
    fun delete(
        endpoint: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val request = Request.Builder()
            .url("$BASE_URL$endpoint")
            .delete()
            .build()

        executeRequest(request, callback)
    }

    /**
     * Executes the request and handles the callback
     */
    private fun executeRequest(request: Request, callback: (Boolean, String?) -> Unit) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                callback(response.isSuccessful, body)
            }
        })
    }
}
