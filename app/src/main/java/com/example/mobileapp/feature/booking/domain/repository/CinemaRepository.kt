package com.example.mobileapp.feature.booking.domain.repository

import com.example.mobileapp.feature.booking.domain.model.Cinema

interface CinemaRepository {
    suspend fun getCinemasByMovieId(movieId: Long, city: String? = null): Result<List<Cinema>>
}
