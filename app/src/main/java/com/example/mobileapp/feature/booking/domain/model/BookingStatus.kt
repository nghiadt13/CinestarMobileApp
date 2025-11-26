package com.example.mobileapp.feature.booking.domain.model

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    EXPIRED;

    companion object {
        fun fromString(value: String): BookingStatus {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                PENDING
            }
        }
    }
}
