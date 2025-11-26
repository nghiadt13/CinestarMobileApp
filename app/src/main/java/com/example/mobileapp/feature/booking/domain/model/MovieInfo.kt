package com.example.mobileapp.feature.booking.domain.model

import java.math.BigDecimal

data class MovieInfo(
    val id: Long,
    val title: String,
    val posterUrl: String?,
    val genres: List<String>,
    val duration: Int?,
    val durationFormatted: String?,
    val ratingAvg: BigDecimal
) {
    val genresText: String
        get() = genres.joinToString(" - ")

    val metaText: String
        get() = if (durationFormatted != null) {
            "$genresText - $durationFormatted"
        } else {
            genresText
        }
}
