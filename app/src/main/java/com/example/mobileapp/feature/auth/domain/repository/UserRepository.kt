package com.example.mobileapp.feature.auth.domain.repository

import com.example.mobileapp.feature.auth.domain.model.LoginResult
import com.example.mobileapp.feature.auth.domain.model.User

interface UserRepository {

    suspend fun getUsers(): Result<List<User>>

    suspend fun login(email: String, password: String): Result<LoginResult>
}
