package com.example.mobileapp.feature.booking.domain.usecase

import com.example.mobileapp.feature.booking.domain.model.Cinema
import com.example.mobileapp.feature.booking.domain.repository.CinemaRepository
import javax.inject.Inject

class GetCinemasUseCase @Inject constructor(
    private val cinemaRepository: CinemaRepository
) {
    suspend operator fun invoke(movieId: Long, city: String? = null): Result<List<Cinema>> {
        return cinemaRepository.getCinemasByMovieId(movieId, city)
    }
}
