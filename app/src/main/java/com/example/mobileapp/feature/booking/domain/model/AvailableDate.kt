package com.example.mobileapp.feature.booking.domain.model

data class AvailableDate(
    val date: String,
    val dayOfWeek: String,
    val dayOfWeekShort: String,
    val dayNumber: Int,
    val monthShort: String,
    val isToday: Boolean
)
