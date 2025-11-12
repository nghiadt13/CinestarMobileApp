package com.example.mobileapp.feature.homepage.domain.usecase

import com.example.mobileapp.feature.homepage.domain.model.NewsItem
import com.example.mobileapp.feature.homepage.domain.repository.NewsRepository
import javax.inject.Inject

class NewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Result<List<NewsItem>> {
        return newsRepository.getAllCurrentNews()
    }
}
