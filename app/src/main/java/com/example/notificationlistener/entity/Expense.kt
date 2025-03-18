package com.example.notificationlistener.entity

data class Expense(
    val name: String,
    val value: Int,
    val bank: String,
    val card: String,
    val categoryId: Int,
    val timestamp: String,
)