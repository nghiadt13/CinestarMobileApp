package com.example.mobileapp.feature.homepage.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MembershipTierDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("description")
    val description: String
)
