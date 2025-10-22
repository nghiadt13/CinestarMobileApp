package com.example.mobileapp.feature.auth.data.remote.api

import com.example.mobileapp.feature.auth.data.remote.dto.LoginRequestDto
import com.example.mobileapp.feature.auth.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login") suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
}
