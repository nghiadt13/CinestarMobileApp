package com.example.mobileapp.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
        @SerialName("success") val success: Boolean,
        @SerialName("message") val message: String,
        @SerialName("token") val token: String?,
        @SerialName("user") val user: UserDto?
)
