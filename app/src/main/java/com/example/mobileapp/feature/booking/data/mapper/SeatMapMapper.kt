package com.example.mobileapp.feature.booking.data.mapper

import com.example.mobileapp.feature.booking.data.remote.dto.SeatInfoDto
import com.example.mobileapp.feature.booking.data.remote.dto.SeatMapResponse
import com.example.mobileapp.feature.booking.domain.model.Seat
import com.example.mobileapp.feature.booking.domain.model.SeatMap
import com.example.mobileapp.feature.booking.domain.model.SeatStatus
import com.example.mobileapp.feature.booking.domain.model.SeatType

object SeatMapMapper {

    fun mapToSeatMap(response: SeatMapResponse): SeatMap {
        return SeatMap(
            showtimeId = response.showtimeId,
            screenId = response.screenId,
            screenName = response.screenName,
            rows = response.rows,
            columns = response.columns,
            seats = response.seats.map { mapToSeat(it) },
            prices = response.prices
        )
    }

    private fun mapToSeat(dto: SeatInfoDto): Seat {
        return Seat(
            id = dto.id,
            seatId = dto.seatId,
            row = dto.row,
            rowLabel = dto.rowLabel,
            column = dto.column,
            columnLabel = dto.columnLabel,
            status = SeatStatus.fromString(dto.status),
            type = SeatType.fromString(dto.type),
            price = dto.price,
            isSelected = false
        )
    }
}
