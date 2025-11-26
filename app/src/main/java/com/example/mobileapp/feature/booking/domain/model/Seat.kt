package com.example.mobileapp.feature.booking.domain.model

import java.math.BigDecimal

data class Seat(
    val id: String,
    val seatId: Long,
    val row: Int,
    val rowLabel: String,
    val column: Int,
    val columnLabel: String,
    val status: SeatStatus,
    val type: SeatType,
    val price: BigDecimal,
    val isSelected: Boolean = false
) {
    val displayName: String
        get() = id

    val isSelectable: Boolean
        get() = status == SeatStatus.AVAILABLE || isSelected
}
