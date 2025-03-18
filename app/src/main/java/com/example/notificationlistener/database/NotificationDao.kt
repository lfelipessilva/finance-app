package com.example.notificationlistener.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: Notification)

    @Query("SELECT * FROM notification WHERE title LIKE 'Compra%' ORDER BY timestamp DESC")
    suspend fun getAllNotifications(): List<Notification>
}