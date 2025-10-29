package com.example.mobileapp.feature.homepage.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieItemDto (
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("synopsis")
    val synopsis: String,

    @SerializedName("durationMin")
    val durationMin: Int,

    @SerializedName("releaseDate")
    val releaseDate: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("posterUrl")
    val posterUrl: String,

    @SerializedName("trailerUrl")
    val trailerUrl: String,

    @SerializedName("ratingAvg")
    val ratingAvg: Double,

    @SerializedName("ratingCount")
    val ratingCount: Int,

    @SerializedName("genres")
    val genres: List<GenreDto>? = null

)