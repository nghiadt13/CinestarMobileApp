package com.example.mobileapp.feature.auth.domain.model

data class LoginResult(
        val success: Boolean,
        val message: String,
        val token: String?,
        val user: User?
)
