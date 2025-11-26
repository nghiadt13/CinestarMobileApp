package com.example.mobileapp.feature.booking.domain.model

data class CinemaInfo(
    val id: Long,
    val name: String,
    val address: String,
    val city: String,
    val imageUrl: String?
)
