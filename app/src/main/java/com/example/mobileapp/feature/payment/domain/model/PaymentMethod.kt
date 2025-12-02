package com.example.mobileapp.feature.payment.domain.model

data class PaymentMethod(
    val id: String,
    val name: String,
    val description: String,
    val iconType: PaymentIconType,
    val isSelected: Boolean = false
)

enum class PaymentIconType {
    VIETQR,
    MOMO,
    ZALOPAY
}
