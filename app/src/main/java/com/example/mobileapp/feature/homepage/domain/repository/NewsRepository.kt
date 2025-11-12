package com.example.mobileapp.feature.homepage.domain.repository

import com.example.mobileapp.feature.homepage.domain.model.NewsItem

interface NewsRepository {
    suspend fun getAllCurrentNews(): Result<List<NewsItem>>
}
