package com.example.notificationlistener.data.remote.dto

data class ListExpenseFilterDto(
    val category: String? = null,
    val name: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)