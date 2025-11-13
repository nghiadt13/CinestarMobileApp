package com.example.mobileapp.feature.moviedetail.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FormatDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("code")
    val code: String,

    @SerializedName("label")
    val label: String
)
