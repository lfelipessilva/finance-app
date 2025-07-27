package com.example.finad.data.remote

import android.content.Context
import com.example.finad.data.local.SessionManager
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

object ApiClient {

    private const val BASE_URL = "https://xenacious-tern-luis-finance-e5c5636e.koyeb.app/api/v1"

    private lateinit var sessionManager: SessionManager

    fun initialize(context: Context) {
        sessionManager = SessionManager.getInstance(context)
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .callTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(AuthInterceptor())
                .addInterceptor(
                        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                )
                .build()
    }

    /** Interceptor to add token automatically */
    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val token = sessionManager.getAccessToken()
            val newRequest =
                    if (!token.isNullOrEmpty()) {
                        request.newBuilder().addHeader("Authorization", "Bearer $token").build()
                    } else request

            return chain.proceed(newRequest)
        }
    }

    fun post(endpoint: String, jsonBody: JSONObject, callback: (Boolean, String?) -> Unit) {
        val requestBody =
                jsonBody.toString()
                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder().url("$BASE_URL$endpoint").post(requestBody).build()

        executeRequest(request, callback)
    }

    fun get(endpoint: String, callback: (Boolean, String?) -> Unit) {
        val request = Request.Builder().url("$BASE_URL$endpoint").get().build()

        executeRequest(request, callback)
    }

    fun put(endpoint: String, jsonBody: JSONObject, callback: (Boolean, String?) -> Unit) {
        val requestBody =
                jsonBody.toString()
                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder().url("$BASE_URL$endpoint").put(requestBody).build()

        executeRequest(request, callback)
    }

    fun delete(endpoint: String, callback: (Boolean, String?) -> Unit) {
        val request = Request.Builder().url("$BASE_URL$endpoint").delete().build()

        executeRequest(request, callback)
    }

    private fun executeRequest(request: Request, callback: (Boolean, String?) -> Unit) {
        client.newCall(request)
                .enqueue(
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                                callback(false, e.message)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val body = response.body?.string()
                                callback(response.isSuccessful, body)
                            }
                        }
                )
    }
}
