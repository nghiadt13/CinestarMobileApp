package com.example.mobileapp.feature.payment.domain.model

data class BookingInfo(
    val movieTitle: String,
    val moviePosterUrl: String,
    val genre: String,
    val format: String,
    val duration: String,
    val cinemaName: String,
    val showtime: String,
    val showdate: String,
    val seats: List<String>,
    val room: String,
    val ticketPrice: Long,
    val ticketCount: Int
)
