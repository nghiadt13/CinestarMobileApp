package com.example.mobileapp.feature.booking.data.remote.api

import com.example.mobileapp.feature.booking.data.remote.dto.CinemaDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CinemaApi {
    @GET("/api/cinemas")
    suspend fun getCinemasByMovieId(
        @Query("movieId") movieId: Long,
        @Query("city") city: String? = null
    ): List<CinemaDto>
}
