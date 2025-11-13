package com.example.mobileapp.feature.moviedetail.domain.model

import com.example.mobileapp.feature.homepage.data.remote.dto.GenreDto
import com.example.mobileapp.feature.moviedetail.data.remote.dto.FormatDto
import com.example.mobileapp.feature.moviedetail.data.remote.dto.MovieStatus

data class MovieDetail(
    val id: Long,
    val title: String,
    val synopsis: String?,
    val durationMin: Short?,
    val releaseDate: String?,
    val status: MovieStatus,
    val posterUrl: String?,
    val trailerUrl: String?,
    val ratingAvg: Double,
    val ratingCount: Int,
    val genres: List<GenreDto>,
    val formats: List<FormatDto>
) {
    val genreNames: String
        get() = genres.joinToString(", ") { it.name }

    val formatCodes: String
        get() = formats.joinToString(", ") { it.code }

    val durationText: String
        get() = durationMin?.let { "${it} phút" } ?: "Không rõ"

    val ratingText: String
        get() = String.format("%.1f", ratingAvg)
}
