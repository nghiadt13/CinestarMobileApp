package com.example.mobileapp.feature.booking.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CinemaDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("district")
    val district: String? = null,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("latitude")
    val latitude: BigDecimal? = null,

    @SerializedName("longitude")
    val longitude: BigDecimal? = null,

    @SerializedName("imageUrl")
    val imageUrl: String? = null
)
