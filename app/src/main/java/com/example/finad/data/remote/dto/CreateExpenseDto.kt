package com.example.finad.data.remote.dto

data class CreateExpenseDto(
    val name: String,
    val value: Int,
    val bank: String,
    val card: String,
    val categoryId: Int?,
    val timestamp: String,
)