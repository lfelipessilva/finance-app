package com.example.finad.data.remote

import com.example.finad.data.remote.dto.ListCategoriesResponseDto
import com.squareup.moshi.Moshi

object CategoryService {
    fun getAllCategories(
        callback: (Boolean, ListCategoriesResponseDto?) -> Unit
    ) {
        ApiClient.get("/categories") { success, response ->
            if (success && response != null) {
                try {
                    val moshi = Moshi.Builder().build()
                    val adapter = moshi.adapter(ListCategoriesResponseDto::class.java)

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
