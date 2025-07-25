package com.example.finad.data.remote

import com.example.finad.data.remote.dto.AuthenticateResponseDto
import com.squareup.moshi.Moshi
import org.json.JSONObject

object AuthService {
    fun authenticate(idToken: String, callback: (Boolean, AuthenticateResponseDto?) -> Unit) {
        val body = JSONObject().apply {
            put("id_token", idToken)
        }

        ApiClient.post("/auth", jsonBody = body ) { success, response ->
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
