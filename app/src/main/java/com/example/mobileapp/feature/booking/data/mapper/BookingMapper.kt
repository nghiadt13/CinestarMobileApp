package com.example.mobileapp.feature.booking.data.mapper

import com.example.mobileapp.feature.booking.data.remote.dto.BookingResponseDto
import com.example.mobileapp.feature.booking.domain.model.Booking
import com.example.mobileapp.feature.booking.domain.model.BookingStatus

object BookingMapper {

    fun mapToBooking(dto: BookingResponseDto): Booking {
        return Booking(
            id = dto.id,
            bookingCode = dto.bookingCode,
            status = BookingStatus.fromString(dto.status),
            movieTitle = dto.movie.title,
            cinemaName = dto.cinema.name,
            showtimeDate = dto.showtime.date,
            showtimeTime = dto.showtime.time,
            screenName = dto.showtime.screenName,
            seats = dto.seats,
            totalPrice = dto.totalPrice,
            expiresAt = dto.expiresAt,
            createdAt = dto.createdAt
        )
    }
}
