package com.orbita.finad.data.remote.dto

import com.orbita.finad.data.remote.entity.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticateResponseDto(
        val user: User,
        @Json(name = "access_token") val accessToken: String,
)
