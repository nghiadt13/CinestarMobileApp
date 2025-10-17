package com.example.mobileapp.feature.auth.ui.viewmodel

import com.example.mobileapp.feature.auth.domain.model.LoginResult
import com.example.mobileapp.feature.auth.domain.model.User

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data class LoginSuccess(val loginResult: LoginResult) : AuthState
    data class UsersLoaded(val users: List<User>) : AuthState
    data class Error(val message: String) : AuthState
}
