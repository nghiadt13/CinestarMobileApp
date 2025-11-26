package com.example.mobileapp.feature.booking.domain.usecase

import com.example.mobileapp.feature.booking.domain.repository.BookingRepository
import javax.inject.Inject

class LockSeatsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        userId: Long,
        showtimeId: Long,
        seatIds: List<Long>
    ): Boolean {
        return repository.lockSeats(userId, showtimeId, seatIds)
    }
}
