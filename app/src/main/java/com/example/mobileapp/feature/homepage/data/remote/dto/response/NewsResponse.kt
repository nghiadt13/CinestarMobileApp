package com.example.mobileapp.feature.homepage.data.remote.dto.response

import com.example.mobileapp.feature.homepage.data.remote.dto.NewsDto
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<NewsDto>
)