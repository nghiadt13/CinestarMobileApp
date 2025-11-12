package com.example.mobileapp.feature.homepage.data.repository

import com.example.mobileapp.feature.homepage.data.mapper.NewsMapper
import com.example.mobileapp.feature.homepage.data.remote.api.NewsApi
import com.example.mobileapp.feature.homepage.domain.model.NewsItem
import com.example.mobileapp.feature.homepage.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {
    override suspend fun getAllCurrentNews(): Result<List<NewsItem>> {
        return try {
            val response = newsApi.getAllCurrentNews()
            val newsItems = NewsMapper.toDomainList(response)
            Result.success(newsItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
