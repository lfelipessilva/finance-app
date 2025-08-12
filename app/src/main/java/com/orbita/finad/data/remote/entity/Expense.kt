package com.orbita.finad.data.remote.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Expense(
        val id: Int,
        val name: String,
        val description: String?,
        val value: Int,
        val bank: String,
        val card: String,
        val categoryId: Int?,
        val timestamp: String,
        val category: Category?,
        val tags: List<Tag>,
)

@JsonClass(generateAdapter = true)
data class ExpenseByCategory(
        val category_id: Int,
        val category_name: String,
        val category_color: String,
        val total_value: Int,
)
