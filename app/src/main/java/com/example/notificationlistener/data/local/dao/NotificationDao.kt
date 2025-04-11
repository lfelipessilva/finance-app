package com.example.notificationlistener.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notificationlistener.data.local.entity.Notification

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: Notification)

    @Query("SELECT * FROM notification WHERE title LIKE 'Compra%' ORDER BY timestamp DESC")
    suspend fun getAllNotifications(): List<Notification>
}