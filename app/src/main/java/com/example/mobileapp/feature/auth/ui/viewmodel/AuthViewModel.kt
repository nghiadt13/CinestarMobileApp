package com.example.mobileapp.feature.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.core.security.TokenManager
import com.example.mobileapp.feature.auth.domain.usecase.LoginUseCase
import com.example.mobileapp.feature.auth.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel
@Inject
constructor(
        private val getUserUseCase: UserUseCase,
        private val loginUseCase: LoginUseCase,
        private val tokenManager: TokenManager
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            loginUseCase(email, password)
                    .onSuccess { loginResult ->
                        // Kiểm tra success field từ API response
                        if (loginResult.success &&
                                        loginResult.token != null &&
                                        loginResult.user != null
                        ) {
                            // Lưu token vào EncryptedSharedPreferences
                            tokenManager.saveToken(loginResult.token)
                            _authState.value = AuthState.LoginSuccess(loginResult)
                        } else {
                            // API trả về success = false
                            _authState.value = AuthState.Error(loginResult.message)
                        }
                    }
                    .onFailure { exception ->
                        // Network error hoặc exception khác
                        _authState.value =
                                AuthState.Error(exception.message ?: "Không thể kết nối đến server")
                    }
        }
    }

    fun logout() {
        tokenManager.clearToken()
        _authState.value = AuthState.Idle
    }

    fun loadUsers() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            getUserUseCase()
                    .onSuccess { users -> _authState.value = AuthState.UsersLoaded(users) }
                    .onFailure { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Cannot load users")
                    }
        }
    }
}
