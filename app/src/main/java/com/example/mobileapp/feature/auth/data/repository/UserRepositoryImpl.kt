package com.example.mobileapp.feature.auth.data.repository

import com.example.mobileapp.feature.auth.data.mapper.toLoginResult
import com.example.mobileapp.feature.auth.data.mapper.toUser
import com.example.mobileapp.feature.auth.data.remote.api.AuthApi
import com.example.mobileapp.feature.auth.data.remote.api.UserApi
import com.example.mobileapp.feature.auth.data.remote.dto.LoginRequestDto
import com.example.mobileapp.feature.auth.domain.model.LoginResult
import com.example.mobileapp.feature.auth.domain.model.User
import com.example.mobileapp.feature.auth.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(private val userApi: UserApi, private val authApi: AuthApi) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val userDtos = userApi.getUsers()
            Result.success(userDtos.map { it.toUser() })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun login(username: String, password: String): Result<LoginResult> {
        return try {
            val request = LoginRequestDto(username = username, password = password)
            val response = authApi.login(request)
            Result.success(response.toLoginResult())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
