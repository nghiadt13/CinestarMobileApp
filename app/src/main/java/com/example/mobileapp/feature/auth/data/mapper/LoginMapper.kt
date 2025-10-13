package com.example.mobileapp.feature.auth.data.mapper

import com.example.mobileapp.feature.auth.data.remote.dto.LoginResponseDto
import com.example.mobileapp.feature.auth.domain.model.LoginResult

fun LoginResponseDto.toLoginResult(): LoginResult {
    return LoginResult(token = token, user = user.toUser())
}
