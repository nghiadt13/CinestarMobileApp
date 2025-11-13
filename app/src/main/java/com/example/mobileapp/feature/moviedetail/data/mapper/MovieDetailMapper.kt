package com.example.mobileapp.feature.moviedetail.data.mapper

import com.example.mobileapp.feature.moviedetail.data.remote.dto.MovieDetailDto
import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail

object MovieDetailMapper {
    fun toDomain(dto: MovieDetailDto): MovieDetail {
        return MovieDetail(
            id = dto.id,
            title = dto.title,
            synopsis = dto.synopsis,
            durationMin = dto.durationMin,
            releaseDate = dto.releaseDate,
            status = dto.status,
            posterUrl = dto.posterUrl,
            trailerUrl = dto.trailerUrl,
            ratingAvg = dto.ratingAvg,
            ratingCount = dto.ratingCount,
            genres = dto.genres,
            formats = dto.formats
        )
    }
}
