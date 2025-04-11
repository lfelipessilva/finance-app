package com.example.notificationlistener.data.remote.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    val id: Int,
    val name: String,
    val color: String,
    val url: String,
    val icon: String
)