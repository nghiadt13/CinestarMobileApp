package com.example.mobileapp.feature.booking.domain.usecase

import com.example.mobileapp.feature.booking.domain.model.SeatMap
import com.example.mobileapp.feature.booking.domain.repository.BookingRepository
import javax.inject.Inject

class GetSeatMapUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(showtimeId: Long): SeatMap {
        return repository.getSeatMap(showtimeId)
    }
}
