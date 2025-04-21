package com.example.finad.data.remote.dto

import com.example.finad.data.remote.entity.Category
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListCategoriesResponseDto(
    val data: List<Category>,
)