package com.example.mobileapp.feature.homepage.data.mapper

import com.example.mobileapp.feature.homepage.data.remote.dto.NewsDto
import com.example.mobileapp.feature.homepage.domain.model.NewsItem

object NewsMapper {
    fun toDomainMapper(dto: NewsDto): NewsItem {
        return NewsItem(
            id = dto.id,
            title = dto.title,
            content = dto.content,
            imageUrl = dto.imageUrl,
            publishedAt = dto.publishedAt
        )
    }

    fun toDomainList(dtoList: List<NewsDto>): List<NewsItem> {
        return dtoList.map {
            toDomainMapper(it)
        }
    }
}
