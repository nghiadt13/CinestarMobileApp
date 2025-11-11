package com.example.mobileapp.feature.homepage.data.remote.dto.response

import com.example.mobileapp.feature.homepage.data.remote.dto.MovieItemDto
import com.google.gson.annotations.SerializedName

data class MovieResponse (
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<MovieItemDto>
)