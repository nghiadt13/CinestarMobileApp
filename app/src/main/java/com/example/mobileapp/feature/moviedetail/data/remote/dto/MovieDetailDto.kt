package com.example.mobileapp.feature.moviedetail.data.remote.dto

import com.example.mobileapp.feature.homepage.data.remote.dto.GenreDto
import com.google.gson.annotations.SerializedName

data class MovieDetailDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("synopsis")
    val synopsis: String?,

    @SerializedName("durationMin")
    val durationMin: Short?,

    @SerializedName("releaseDate")
    val releaseDate: String?,

    @SerializedName("status")
    val status: MovieStatus,

    @SerializedName("posterUrl")
    val posterUrl: String?,

    @SerializedName("trailerUrl")
    val trailerUrl: String?,

    @SerializedName("ratingAvg")
    val ratingAvg: Double,

    @SerializedName("ratingCount")
    val ratingCount: Int,

    @SerializedName("genres")
    val genres: List<GenreDto>,

    @SerializedName("formats")
    val formats: List<FormatDto>
)
