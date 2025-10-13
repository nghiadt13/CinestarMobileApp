package com.example.mobileapp.feature.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
constructor(private val getUserUseCase: UserUseCase, private val loginUseCase: LoginUseCase) :
        ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            loginUseCase(email, password)
                    .onSuccess { loginResult ->
                        _authState.value = AuthState.LoginSuccess(loginResult)
                    }
                    .onFailure { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Login failed")
                    }
        }
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
