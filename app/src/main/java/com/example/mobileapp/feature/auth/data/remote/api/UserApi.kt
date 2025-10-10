package com.example.mobileapp.feature.auth.data.remote.api

import com.example.mobileapp.feature.auth.data.remote.dto.UserDto
import retrofit2.http.GET

interface UserApi {

    @GET("/api/users")
    suspend fun getUsers() : List<UserDto>

}