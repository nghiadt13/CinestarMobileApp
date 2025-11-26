package com.example.mobileapp.feature.booking.domain.usecase

import com.example.mobileapp.feature.booking.domain.model.Booking
import com.example.mobileapp.feature.booking.domain.repository.BookingRepository
import java.math.BigDecimal
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        userId: Long,
        showtimeId: Long,
        seatIds: List<Long>,
        totalPrice: BigDecimal
    ): Booking {
        return repository.createBooking(userId, showtimeId, seatIds, totalPrice)
    }
}
