package com.example.mobileapp.feature.booking.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Response from GET /api/booking/data?movieId={movieId}&cinemaId={cinemaId}
 * Contains all data needed for the booking ticket screen
 */
data class BookingDataResponse(
    @SerializedName("movie")
    val movie: MovieInfoDto,

    @SerializedName("cinema")
    val cinema: CinemaInfoDto,

    @SerializedName("availableDates")
    val availableDates: List<AvailableDateDto>,

    @SerializedName("formats")
    val formats: List<ScreeningFormatDto>,

    @SerializedName("showtimes")
    val showtimes: Map<String, Map<String, List<ShowtimeSlotDto>>>
)

data class MovieInfoDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("posterUrl")
    val posterUrl: String?,

    @SerializedName("genres")
    val genres: List<String>,

    @SerializedName("duration")
    val duration: Int?,

    @SerializedName("durationFormatted")
    val durationFormatted: String?,

    @SerializedName("ratingAvg")
    val ratingAvg: BigDecimal
)

data class CinemaInfoDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("imageUrl")
    val imageUrl: String?
)

data class AvailableDateDto(
    @SerializedName("date")
    val date: String,

    @SerializedName("dayOfWeek")
    val dayOfWeek: String,

    @SerializedName("dayOfWeekShort")
    val dayOfWeekShort: String,

    @SerializedName("dayNumber")
    val dayNumber: Int,

    @SerializedName("monthShort")
    val monthShort: String,

    @SerializedName("isToday")
    val isToday: Boolean
)

data class ScreeningFormatDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("isDefault")
    val isDefault: Boolean
)

data class ShowtimeSlotDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("time")
    val time: String,

    @SerializedName("startTime")
    val startTime: String,

    @SerializedName("endTime")
    val endTime: String,

    @SerializedName("screenId")
    val screenId: Long,

    @SerializedName("screenName")
    val screenName: String,

    @SerializedName("formatCode")
    val formatCode: String?,

    @SerializedName("availableSeats")
    val availableSeats: Int,

    @SerializedName("totalSeats")
    val totalSeats: Int,

    @SerializedName("prices")
    val prices: Map<String, BigDecimal>,

    @SerializedName("isAlmostFull")
    val isAlmostFull: Boolean,

    @SerializedName("isSoldOut")
    val isSoldOut: Boolean
)
