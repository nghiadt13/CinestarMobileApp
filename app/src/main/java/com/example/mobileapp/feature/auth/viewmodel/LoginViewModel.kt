package com.example.mobileapp.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.auth.domain.model.LoginResult
import com.example.mobileapp.feature.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            loginUseCase(username, password)
                    .onSuccess { result ->
                        if (result.success && result.token != null && result.user != null) {
                            _loginState.value = LoginState.Success(result)
                        } else {
                            _loginState.value = LoginState.Error(result.message)
                        }
                    }
                    .onFailure { exception ->
                        _loginState.value =
                                LoginState.Error(
                                        exception.message ?: "Không thể kết nối đến server"
                                )
                    }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val loginResult: LoginResult) : LoginState()
    data class Error(val message: String) : LoginState()
}
