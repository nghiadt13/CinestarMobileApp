package com.example.mobileapp.feature.booking.domain.usecase

import com.example.mobileapp.feature.booking.domain.model.BookingData
import com.example.mobileapp.feature.booking.domain.repository.BookingRepository
import javax.inject.Inject

class GetBookingDataUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(movieId: Long, cinemaId: Long): BookingData {
        return repository.getBookingData(movieId, cinemaId)
    }
}
