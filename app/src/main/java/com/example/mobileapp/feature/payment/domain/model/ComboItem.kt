package com.example.mobileapp.feature.payment.domain.model

data class ComboItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Long,
    val imageUrl: String,
    val quantity: Int = 0
)
