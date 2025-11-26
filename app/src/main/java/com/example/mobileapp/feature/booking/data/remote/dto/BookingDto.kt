package com.example.mobileapp.feature.booking.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Request body for POST /api/bookings
 */
data class CreateBookingRequest(
    @SerializedName("showtimeId")
    val showtimeId: Long,

    @SerializedName("seatIds")
    val seatIds: List<Long>,

    @SerializedName("totalPrice")
    val totalPrice: BigDecimal
)

/**
 * Response from POST /api/bookings
 */
data class BookingResponseDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("bookingCode")
    val bookingCode: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("movie")
    val movie: BookingMovieDto,

    @SerializedName("cinema")
    val cinema: BookingCinemaDto,

    @SerializedName("showtime")
    val showtime: BookingShowtimeDto,

    @SerializedName("seats")
    val seats: List<String>,

    @SerializedName("totalPrice")
    val totalPrice: BigDecimal,

    @SerializedName("expiresAt")
    val expiresAt: String,

    @SerializedName("createdAt")
    val createdAt: String
)

data class BookingMovieDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String
)

data class BookingCinemaDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String
)

data class BookingShowtimeDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("date")
    val date: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("screenName")
    val screenName: String
)

/**
 * Response for seat lock operations
 */
data class SeatLockResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)
