package com.example.notificationlistener.data.remote.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    val id: Int,
    val name: String,
    val color: String,
)