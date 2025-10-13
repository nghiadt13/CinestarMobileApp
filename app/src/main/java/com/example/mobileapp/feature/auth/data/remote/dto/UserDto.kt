package com.example.mobileapp.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(

    @SerialName("id")
    val id: Long,

    @SerialName("email")
    val email: String?,

    @SerialName("phoneNumber")
    val phoneNumber: String?,

    @SerialName("displayName")
    val displayName: String,

    @SerialName("avatarUrl")
    val avatarUrl: String?,

    @SerialName("isActive")
    val isActive: Boolean,

    @SerialName("emailVerifiedAt")
    val emailVerifiedAt: String?,

    @SerialName("phoneNumberVerifiedAt")
    val phoneNumberVerifiedAt: String?,

    @SerialName("lastLoginAt")
    val lastLoginAt: String?,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String
)