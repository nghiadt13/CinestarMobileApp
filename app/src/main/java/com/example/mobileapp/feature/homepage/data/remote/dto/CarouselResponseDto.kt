package com.example.mobileapp.feature.homepage.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CarouselResponseDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<CarouselItemDto>
)
