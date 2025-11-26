package com.example.mobileapp.feature.booking.domain.model

import java.math.BigDecimal

data class Booking(
    val id: Long,
    val bookingCode: String,
    val status: BookingStatus,
    val movieTitle: String,
    val cinemaName: String,
    val showtimeDate: String,
    val showtimeTime: String,
    val screenName: String,
    val seats: List<String>,
    val totalPrice: BigDecimal,
    val expiresAt: String,
    val createdAt: String
) {
    val seatsDisplay: String
        get() = seats.joinToString(", ")

    val isPending: Boolean
        get() = status == BookingStatus.PENDING

    val isConfirmed: Boolean
        get() = status == BookingStatus.CONFIRMED
}
