package com.example.mobileapp.feature.auth.data.mapper

import com.example.mobileapp.feature.auth.data.remote.dto.UserDto
import com.example.mobileapp.feature.auth.domain.model.User

fun UserDto.toUser(): User {
    return User(
            id = id,
            email = email,
            phoneNumber = phoneNumber,
            displayName = displayName,
            avatarUrl = avatarUrl,
            isActive = isActive,
            emailVerifiedAt = emailVerifiedAt,
            phoneNumberVerifiedAt = phoneNumberVerifiedAt,
            lastLoginAt = lastLoginAt
    )
}
