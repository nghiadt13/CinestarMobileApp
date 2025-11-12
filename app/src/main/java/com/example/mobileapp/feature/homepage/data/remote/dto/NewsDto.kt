package com.example.mobileapp.feature.homepage.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("publishedAt")
    val publishedAt: String
)