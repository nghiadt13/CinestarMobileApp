package com.example.mobileapp.feature.booking.domain.model

import java.math.BigDecimal

data class SeatMap(
    val showtimeId: Long,
    val screenId: Long,
    val screenName: String,
    val rows: Int,
    val columns: Int,
    val seats: List<Seat>,
    val prices: Map<String, BigDecimal>
)
