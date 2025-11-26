package com.example.mobileapp.feature.booking.domain.model

enum class SeatType {
    STANDARD,
    VIP,
    COUPLE,
    DELUXE;

    companion object {
        fun fromString(value: String): SeatType {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                STANDARD
            }
        }
    }
}
