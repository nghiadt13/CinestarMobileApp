package com.example.mobileapp.feature.booking.domain.model

data class BookingData(
    val movie: MovieInfo,
    val cinema: CinemaInfo,
    val availableDates: List<AvailableDate>,
    val formats: List<ScreeningFormat>,
    val showtimes: Map<String, Map<String, List<Showtime>>>
) {
    fun getShowtimesForDateAndFormat(date: String, formatCode: String): List<Showtime> {
        return showtimes[date]?.get(formatCode) ?: emptyList()
    }

    fun getDefaultFormat(): ScreeningFormat? {
        return formats.find { it.isDefault } ?: formats.firstOrNull()
    }

    fun getTodayDate(): AvailableDate? {
        return availableDates.find { it.isToday } ?: availableDates.firstOrNull()
    }
}
