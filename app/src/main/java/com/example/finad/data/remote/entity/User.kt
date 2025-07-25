package com.example.finad.data.remote.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val email: String,
    val name: String,
    val provider: String,
    val providerUserId: String,
    val profilePicture: String
)