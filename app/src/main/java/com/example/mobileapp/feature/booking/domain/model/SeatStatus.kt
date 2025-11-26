package com.example.mobileapp.feature.booking.domain.model

enum class SeatStatus {
    AVAILABLE,
    SELECTED,
    BOOKED,
    LOCKED,
    BLOCKED;

    companion object {
        fun fromString(value: String): SeatStatus {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                AVAILABLE
            }
        }
    }
}
