package com.example.mobileapp.feature.booking.data.repository

import com.example.mobileapp.feature.booking.data.mapper.CinemaMapper
import com.example.mobileapp.feature.booking.data.remote.api.CinemaApi
import com.example.mobileapp.feature.booking.domain.model.Cinema
import com.example.mobileapp.feature.booking.domain.repository.CinemaRepository
import javax.inject.Inject

class CinemaRepositoryImpl @Inject constructor(
    private val cinemaApi: CinemaApi
) : CinemaRepository {

    override suspend fun getCinemasByMovieId(movieId: Long, city: String?): Result<List<Cinema>> {
        return try {
            val response = cinemaApi.getCinemasByMovieId(movieId, city)
            val cinemas = CinemaMapper.toDomainList(response)
            Result.success(cinemas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
