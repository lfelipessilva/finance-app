package com.example.notificationlistener.data.remote.dto

import com.example.notificationlistener.data.remote.entity.Expense
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListExpenseResponseDto(
    val data: List<Expense>,
    val sum: Int,
    val summary: ListExpenseResponseSummary
)

@JsonClass(generateAdapter = true)
data class ListExpenseResponseSummary(
    val total: Int,
    val page: Int,
    @Json(name = "page_size") val pageSize: Int
)
