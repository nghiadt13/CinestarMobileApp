package com.example.mobileapp.feature.auth.domain.usecase

import com.example.mobileapp.feature.auth.domain.model.LoginResult
import com.example.mobileapp.feature.auth.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(username: String, password: String): Result<LoginResult> {
        return userRepository.login(username, password)
    }
}
