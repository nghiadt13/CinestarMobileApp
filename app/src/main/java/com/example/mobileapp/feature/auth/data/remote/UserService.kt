package com.example.mobileapp.feature.auth.data.remote

import com.example.mobileapp.feature.auth.data.remote.dto.UserDto
import retrofit2.http.GET

interface UserService {
    
    @GET("/api/users")
    suspend fun getUsers() : List<UserDto>

}