package com.example.mobileapp.feature.booking.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Response from GET /api/booking/showtimes/{showtimeId}/seats
 * Contains seat layout and status for a specific showtime
 */
data class SeatMapResponse(
    @SerializedName("showtimeId")
    val showtimeId: Long,

    @SerializedName("screenId")
    val screenId: Long,

    @SerializedName("screenName")
    val screenName: String,

    @SerializedName("rows")
    val rows: Int,

    @SerializedName("columns")
    val columns: Int,

    @SerializedName("seats")
    val seats: List<SeatInfoDto>,

    @SerializedName("prices")
    val prices: Map<String, BigDecimal>
)

data class SeatInfoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("seatId")
    val seatId: Long,

    @SerializedName("row")
    val row: Int,

    @SerializedName("rowLabel")
    val rowLabel: String,

    @SerializedName("column")
    val column: Int,

    @SerializedName("columnLabel")
    val columnLabel: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("price")
    val price: BigDecimal
)
