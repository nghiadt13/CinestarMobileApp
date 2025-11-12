package com.example.mobileapp.feature.homepage.domain.model

data class NewsItem(
    val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String,
    val publishedAt: String
)
