package com.example.mobileapp.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
        @SerialName("token") val token: String,
        @SerialName("user") val user: UserDto
)
