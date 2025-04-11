package com.example.notificationlistener.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notificationlistener.data.local.entity.Expense

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM Expense ORDER BY timestamp DESC")
    suspend fun getAllExpenses(): List<Expense>
}