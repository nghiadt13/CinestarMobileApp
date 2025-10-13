package com.example.mobileapp.feature.auth.domain.usecase

import com.example.mobileapp.feature.auth.domain.model.LoginResult
import com.example.mobileapp.feature.auth.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResult> {
        // Validate input
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email and password cannot be empty"))
        }

        return userRepository.login(email, password)
    }
}
