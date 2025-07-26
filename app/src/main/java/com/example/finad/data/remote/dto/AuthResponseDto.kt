package com.example.finad.data.remote.dto

import com.example.finad.data.remote.entity.Expense
import com.example.finad.data.remote.entity.ExpenseByCategory
import com.example.finad.data.remote.entity.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticateResponseDto(
    val user: User,
    @Json(name = "access_token") val accessToken: String,
)