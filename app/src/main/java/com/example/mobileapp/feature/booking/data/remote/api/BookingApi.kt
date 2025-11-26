package com.example.mobileapp.feature.booking.data.remote.api

import com.example.mobileapp.feature.booking.data.remote.dto.*
import retrofit2.http.*

interface BookingApi {

    /**
     * Get all booking data (movie, cinema, dates, formats, showtimes)
     * for the booking ticket screen
     */
    @GET("/api/booking/data")
    suspend fun getBookingData(
        @Query("movieId") movieId: Long,
        @Query("cinemaId") cinemaId: Long
    ): BookingDataResponse

    /**
     * Get seat map for a specific showtime
     */
    @GET("/api/booking/showtimes/{showtimeId}/seats")
    suspend fun getSeatMap(
        @Path("showtimeId") showtimeId: Long
    ): SeatMapResponse

    /**
     * Create a new booking
     */
    @POST("/api/bookings")
    suspend fun createBooking(
        @Header("X-User-Id") userId: Long,
        @Body request: CreateBookingRequest
    ): BookingResponseDto

    /**
     * Lock seats temporarily while user is selecting
     */
    @POST("/api/bookings/seats/lock")
    suspend fun lockSeats(
        @Header("X-User-Id") userId: Long,
        @Query("showtimeId") showtimeId: Long,
        @Query("seatIds") seatIds: String
    ): SeatLockResponse

    /**
     * Unlock seats when user leaves the screen
     */
    @DELETE("/api/bookings/seats/lock")
    suspend fun unlockSeats(
        @Header("X-User-Id") userId: Long,
        @Query("showtimeId") showtimeId: Long
    ): SeatLockResponse

    /**
     * Get booking by ID
     */
    @GET("/api/bookings/{id}")
    suspend fun getBookingById(
        @Path("id") bookingId: Long
    ): BookingResponseDto

    /**
     * Get booking by code
     */
    @GET("/api/bookings/code/{bookingCode}")
    suspend fun getBookingByCode(
        @Path("bookingCode") bookingCode: String
    ): BookingResponseDto

    /**
     * Get user's bookings
     */
    @GET("/api/bookings/user")
    suspend fun getUserBookings(
        @Header("X-User-Id") userId: Long
    ): List<BookingResponseDto>

    /**
     * Cancel a booking
     */
    @POST("/api/bookings/{id}/cancel")
    suspend fun cancelBooking(
        @Header("X-User-Id") userId: Long,
        @Path("id") bookingId: Long
    ): BookingResponseDto
}
