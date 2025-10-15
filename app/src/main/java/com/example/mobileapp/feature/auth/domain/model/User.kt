package com.example.mobileapp.feature.auth.domain.model

data class User(
        val id: Long,
        val email: String?,
        val phoneNumber: String?,
        val displayName: String,
        val avatarUrl: String?,
        val isActive: Boolean,
        val emailVerifiedAt: String?,
        val phoneNumberVerifiedAt: String?,
        val lastLoginAt: String?
)
