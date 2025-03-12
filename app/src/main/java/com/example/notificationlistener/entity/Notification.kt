package com.example.notificationlistener.entity

import java.sql.Timestamp

data class Expense(
    val name: String,
    val value: Number,
    val category: String,
    val timestamp: String,
)