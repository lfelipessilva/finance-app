package com.orbita.finad.data.remote.dto

import com.orbita.finad.data.remote.entity.Expense
import com.orbita.finad.data.remote.entity.ExpenseByCategory
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

@JsonClass(generateAdapter = true)
data class ListExpenseByCategoryResponseDto(
        val data: List<ExpenseByCategory>,
)
