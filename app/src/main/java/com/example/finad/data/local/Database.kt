package com.example.finad.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.finad.data.local.dao.ExpenseDao
import com.example.finad.data.local.dao.NotificationDao
import com.example.finad.data.local.entity.Expense
import com.example.finad.data.local.entity.Notification

@Database(entities = [Notification::class, Expense::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance
                    ?: synchronized(this) {
                        instance ?: createDatabase(context).also { instance = it }
                    }
        }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}
