package com.example.mobileapp.feature.homepage.domain.model

import com.example.mobileapp.feature.homepage.data.remote.dto.GenreDto

data class MovieItem (
    val id : Int,
    val title : String,
    val posterUrl : String?,
    val genres : List<GenreDto>?,
    val durationMin :  Int,
    val ratingAvg : Double
)