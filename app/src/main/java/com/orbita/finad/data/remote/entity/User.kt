package com.orbita.finad.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
        val id: Int,
        val email: String,
        val name: String,
        val provider: String,
        @Json(name = "prover_user_id") val providerUserId: String,
        @Json(name = "profile_picture") val profilePicture: String
)
