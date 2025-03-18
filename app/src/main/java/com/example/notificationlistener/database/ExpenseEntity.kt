package com.example.notificationlistener.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "value") val value: Int,
    @ColumnInfo(name = "card") val card: String,
    @ColumnInfo(name = "bank") val bank: String,
    @ColumnInfo(name = "timestamp") val timestamp: String
)