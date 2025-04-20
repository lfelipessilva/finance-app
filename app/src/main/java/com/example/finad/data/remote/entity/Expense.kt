package com.example.finad.data.remote.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Expense(
    val id: Int,
    val name: String,
    val value: Int,
    val bank: String,
    val card: String,
    val categoryId: Int?,
    val timestamp: String,
    val category: Category?,
    val tags: List<Tag>,
)