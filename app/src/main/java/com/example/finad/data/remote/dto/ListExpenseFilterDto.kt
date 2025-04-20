package com.example.finad.data.remote.dto

import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

data class ListExpenseFilterDto(
    val category: String? = null,
    val name: String? = null,
    val timestampStart: Long? =
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis,
    val timestampEnd: Long? = Date().time,
    val page: Int = 1,
    val pageSize: Int = 50
)