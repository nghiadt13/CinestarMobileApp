package com.example.mobileapp.feature.booking.domain.repository

import com.example.mobileapp.feature.booking.domain.model.Booking
import com.example.mobileapp.feature.booking.domain.model.BookingData
import com.example.mobileapp.feature.booking.domain.model.SeatMap
import java.math.BigDecimal

interface BookingRepository {

    suspend fun getBookingData(movieId: Long, cinemaId: Long): BookingData

    suspend fun getSeatMap(showtimeId: Long): SeatMap

    suspend fun createBooking(
        userId: Long,
        showtimeId: Long,
        seatIds: List<Long>,
        totalPrice: BigDecimal
    ): Booking

    suspend fun lockSeats(
        userId: Long,
        showtimeId: Long,
        seatIds: List<Long>
    ): Boolean

    suspend fun unlockSeats(
        userId: Long,
        showtimeId: Long
    ): Boolean

    suspend fun getBookingById(bookingId: Long): Booking

    suspend fun getBookingByCode(bookingCode: String): Booking

    suspend fun getUserBookings(userId: Long): List<Booking>

    suspend fun cancelBooking(userId: Long, bookingId: Long): Booking
}
