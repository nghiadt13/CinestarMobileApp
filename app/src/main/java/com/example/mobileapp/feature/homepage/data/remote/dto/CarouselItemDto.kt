package com.example.mobileapp.feature.homepage.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CarouselItemDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("linkUrl")
    val linkUrl: String?,

    @SerializedName("displayOrder")
    val displayOrder: Int,

    @SerializedName("isActive")
    val isActive: Boolean,

    @SerializedName("startDate")
    val startDate: String?,

    @SerializedName("endDate")
    val endDate: String?,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String

)