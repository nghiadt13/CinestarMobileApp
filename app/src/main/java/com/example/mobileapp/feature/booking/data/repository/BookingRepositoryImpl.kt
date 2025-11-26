package com.example.mobileapp.feature.booking.data.repository

import com.example.mobileapp.feature.booking.data.mapper.BookingDataMapper
import com.example.mobileapp.feature.booking.data.mapper.BookingMapper
import com.example.mobileapp.feature.booking.data.mapper.SeatMapMapper
import com.example.mobileapp.feature.booking.data.remote.api.BookingApi
import com.example.mobileapp.feature.booking.data.remote.dto.CreateBookingRequest
import com.example.mobileapp.feature.booking.domain.model.Booking
import com.example.mobileapp.feature.booking.domain.model.BookingData
import com.example.mobileapp.feature.booking.domain.model.SeatMap
import com.example.mobileapp.feature.booking.domain.repository.BookingRepository
import java.math.BigDecimal
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingApi: BookingApi
) : BookingRepository {

    override suspend fun getBookingData(movieId: Long, cinemaId: Long): BookingData {
        val response = bookingApi.getBookingData(movieId, cinemaId)
        return BookingDataMapper.mapToBookingData(response)
    }

    override suspend fun getSeatMap(showtimeId: Long): SeatMap {
        val response = bookingApi.getSeatMap(showtimeId)
        return SeatMapMapper.mapToSeatMap(response)
    }

    override suspend fun createBooking(
        userId: Long,
        showtimeId: Long,
        seatIds: List<Long>,
        totalPrice: BigDecimal
    ): Booking {
        val request = CreateBookingRequest(
            showtimeId = showtimeId,
            seatIds = seatIds,
            totalPrice = totalPrice
        )
        val response = bookingApi.createBooking(userId, request)
        return BookingMapper.mapToBooking(response)
    }

    override suspend fun lockSeats(
        userId: Long,
        showtimeId: Long,
        seatIds: List<Long>
    ): Boolean {
        val seatIdsString = seatIds.joinToString(",")
        val response = bookingApi.lockSeats(userId, showtimeId, seatIdsString)
        return response.success
    }

    override suspend fun unlockSeats(userId: Long, showtimeId: Long): Boolean {
        val response = bookingApi.unlockSeats(userId, showtimeId)
        return response.success
    }

    override suspend fun getBookingById(bookingId: Long): Booking {
        val response = bookingApi.getBookingById(bookingId)
        return BookingMapper.mapToBooking(response)
    }

    override suspend fun getBookingByCode(bookingCode: String): Booking {
        val response = bookingApi.getBookingByCode(bookingCode)
        return BookingMapper.mapToBooking(response)
    }

    override suspend fun getUserBookings(userId: Long): List<Booking> {
        val response = bookingApi.getUserBookings(userId)
        return response.map { BookingMapper.mapToBooking(it) }
    }

    override suspend fun cancelBooking(userId: Long, bookingId: Long): Booking {
        val response = bookingApi.cancelBooking(userId, bookingId)
        return BookingMapper.mapToBooking(response)
    }
}
