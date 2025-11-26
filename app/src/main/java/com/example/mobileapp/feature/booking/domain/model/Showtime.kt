package com.example.mobileapp.feature.booking.domain.model

import java.math.BigDecimal

data class Showtime(
    val id: Long,
    val time: String,
    val startTime: String,
    val endTime: String,
    val screenId: Long,
    val screenName: String,
    val formatCode: String?,
    val availableSeats: Int,
    val totalSeats: Int,
    val prices: Map<String, BigDecimal>,
    val isAlmostFull: Boolean,
    val isSoldOut: Boolean
) {
    val minPrice: BigDecimal?
        get() = prices.values.minOrNull()

    val availabilityPercentage: Int
        get() = if (totalSeats > 0) (availableSeats * 100) / totalSeats else 0
}
