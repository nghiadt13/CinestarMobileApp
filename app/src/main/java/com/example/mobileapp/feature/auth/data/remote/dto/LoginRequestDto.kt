package com.example.mobileapp.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
        @SerialName("email") val email: String,
        @SerialName("password") val password: String
)
