package com.example.finad.data.remote.dto

data class ListExpenseFilterDto(
    val category: String? = null,
    val name: String? = null,
    val timestampStart: Long? = null,
    val timestampEnd: Long? = null,
    val page: Int = 1,
    val pageSize: Int = 50
)