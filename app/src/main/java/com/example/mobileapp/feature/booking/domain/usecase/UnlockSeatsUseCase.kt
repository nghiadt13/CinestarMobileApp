package com.example.mobileapp.feature.booking.domain.usecase

import com.example.mobileapp.feature.booking.domain.repository.BookingRepository
import javax.inject.Inject

class UnlockSeatsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(userId: Long, showtimeId: Long): Boolean {
        return repository.unlockSeats(userId, showtimeId)
    }
}
