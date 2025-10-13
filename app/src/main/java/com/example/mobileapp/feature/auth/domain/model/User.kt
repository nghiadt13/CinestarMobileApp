package com.example.mobileapp.feature.auth.domain.model

data class User(
    val id: Long,
    val email: String?,
    val displayName: String?,
    val avatarUrl: String?
)