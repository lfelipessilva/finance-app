package com.orbita.finad.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "app") val app: String,
        @ColumnInfo(name = "text") val text: String,
        @ColumnInfo(name = "subtext") val subtext: String,
        @ColumnInfo(name = "timestamp") val timestamp: String
)
