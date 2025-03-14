package com.example.notificationlistener.entity

data class Expense(
    val name: String,
    val value: Number,
    val bank: String,
    val card: String,
    val category: String,
    val timestamp: String,
)