package com.orbita.finad.data.remote

import com.orbita.finad.data.remote.dto.AuthenticateResponseDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object AuthService {
    fun authenticate(idToken: String, callback: (Boolean, AuthenticateResponseDto?) -> Unit) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val body = moshi.adapter(Map::class.java).toJson(mapOf("id_token" to idToken))

        ApiClient.post("/auth", jsonBody = body) { success, response ->
            if (success && response != null) {
                try {
                    val moshi = Moshi.Builder().build()
                    val adapter = moshi.adapter(AuthenticateResponseDto::class.java)

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
}
