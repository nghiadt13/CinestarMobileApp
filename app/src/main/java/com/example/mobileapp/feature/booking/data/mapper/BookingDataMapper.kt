package com.example.mobileapp.feature.booking.data.mapper

import com.example.mobileapp.feature.booking.data.remote.dto.*
import com.example.mobileapp.feature.booking.domain.model.*

object BookingDataMapper {

    fun mapToBookingData(response: BookingDataResponse): BookingData {
        return BookingData(
            movie = mapToMovieInfo(response.movie),
            cinema = mapToCinemaInfo(response.cinema),
            availableDates = response.availableDates.map { mapToAvailableDate(it) },
            formats = response.formats.map { mapToScreeningFormat(it) },
            showtimes = mapShowtimes(response.showtimes)
        )
    }

    private fun mapToMovieInfo(dto: MovieInfoDto): MovieInfo {
        return MovieInfo(
            id = dto.id,
            title = dto.title,
            posterUrl = dto.posterUrl,
            genres = dto.genres,
            duration = dto.duration,
            durationFormatted = dto.durationFormatted,
            ratingAvg = dto.ratingAvg
        )
    }

    private fun mapToCinemaInfo(dto: CinemaInfoDto): CinemaInfo {
        return CinemaInfo(
            id = dto.id,
            name = dto.name,
            address = dto.address,
            city = dto.city,
            imageUrl = dto.imageUrl
        )
    }

    private fun mapToAvailableDate(dto: AvailableDateDto): AvailableDate {
        return AvailableDate(
            date = dto.date,
            dayOfWeek = dto.dayOfWeek,
            dayOfWeekShort = dto.dayOfWeekShort,
            dayNumber = dto.dayNumber,
            monthShort = dto.monthShort,
            isToday = dto.isToday
        )
    }

    private fun mapToScreeningFormat(dto: ScreeningFormatDto): ScreeningFormat {
        return ScreeningFormat(
            id = dto.id,
            name = dto.name,
            code = dto.code,
            isDefault = dto.isDefault
        )
    }

    private fun mapShowtimes(
        showtimesMap: Map<String, Map<String, List<ShowtimeSlotDto>>>
    ): Map<String, Map<String, List<Showtime>>> {
        return showtimesMap.mapValues { (_, formatMap) ->
            formatMap.mapValues { (_, showtimeList) ->
                showtimeList.map { mapToShowtime(it) }
            }
        }
    }

    private fun mapToShowtime(dto: ShowtimeSlotDto): Showtime {
        return Showtime(
            id = dto.id,
            time = dto.time,
            startTime = dto.startTime,
            endTime = dto.endTime,
            screenId = dto.screenId,
            screenName = dto.screenName,
            formatCode = dto.formatCode,
            availableSeats = dto.availableSeats,
            totalSeats = dto.totalSeats,
            prices = dto.prices,
            isAlmostFull = dto.isAlmostFull,
            isSoldOut = dto.isSoldOut
        )
    }
}
