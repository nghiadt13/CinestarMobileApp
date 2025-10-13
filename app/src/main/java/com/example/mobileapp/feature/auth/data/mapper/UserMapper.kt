package com.example.mobileapp.feature.auth.data.mapper

import com.example.mobileapp.feature.auth.data.remote.dto.UserDto // Giả sử UserDto của bạn ở đây
import com.example.mobileapp.feature.auth.domain.model.User

// Bạn đang viết một extension function (hàm mở rộng).
// Hàm này dùng để chuyển đổi (map) một đối tượng UserDto (lấy từ API) thành một đối tượng User (dùng trong app).
fun UserDto.toUser(): User {
    return User(
        id = id,
        email = email ?: "N/A",
        displayName = displayName,
        avatarUrl = avatarUrl
    )
}