package com.example.mobileapp.feature.auth.domain.usecase

import com.example.mobileapp.feature.auth.domain.model.User
import com.example.mobileapp.feature.auth.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
    }
}