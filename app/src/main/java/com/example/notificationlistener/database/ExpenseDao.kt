package com.example.notificationlistener.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM Expense ORDER BY timestamp DESC")
    suspend fun getAllExpenses(): List<Expense>
}