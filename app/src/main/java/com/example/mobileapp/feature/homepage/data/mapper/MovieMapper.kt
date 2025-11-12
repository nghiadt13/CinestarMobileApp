package com.example.mobileapp.feature.homepage.data.mapper

import com.example.mobileapp.feature.homepage.data.remote.dto.MovieItemDto
import com.example.mobileapp.feature.homepage.domain.model.MovieItem

object MovieMapper {
    fun toDomainMapper(dto : MovieItemDto) : MovieItem {
        return MovieItem(
            id = dto.id ,
            title = dto.title,
            posterUrl = dto.posterUrl,
            genres = dto.genres,
            ratingAvg = dto.ratingAvg,
            durationMin = dto.durationMin
        )
    }
    fun toDomainList(dtoList : List<MovieItemDto>) : List<MovieItem> {
        return dtoList.map {
            toDomainMapper(it)
        }
    }
}