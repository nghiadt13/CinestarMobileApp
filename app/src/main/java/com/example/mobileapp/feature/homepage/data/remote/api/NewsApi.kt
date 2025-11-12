package com.example.mobileapp.feature.homepage.data.remote.api

import com.example.mobileapp.feature.homepage.data.remote.dto.NewsDto
import retrofit2.http.GET

interface NewsApi {
    @GET("/api/news")
    suspend fun getAllCurrentNews(): List<NewsDto>
}