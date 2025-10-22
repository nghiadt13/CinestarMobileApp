package com.example.mobileapp.feature.homepage.domain.model

data class CarouselItem (
    val id: Long,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val linkUrl: String?,
    val displayOrder: Int
)