package com.example.finad.data.remote

import com.example.finad.data.remote.entity.Category
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object CategoryService {

    fun getAllCategories(callback: (Boolean, List<Category>?) -> Unit) {
        ApiClient.get("/categories") { success, response ->
            if (success && response != null) {
                try {
                    val moshi = Moshi.Builder().build()
                    val listType =
                            Types.newParameterizedType(List::class.java, Category::class.java)
                    val adapter = moshi.adapter<List<Category>>(listType)

                    val categories = adapter.fromJson(response)
                    callback(true, categories)
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
