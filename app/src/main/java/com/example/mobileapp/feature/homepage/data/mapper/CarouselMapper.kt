package com.example.mobileapp.feature.homepage.data.mapper

import com.example.mobileapp.feature.homepage.data.remote.dto.CarouselItemDto
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem

object CarouselMapper {
    fun toDomain(dto: CarouselItemDto) : CarouselItem {
        return CarouselItem(
                id = dto.id,
                title = dto.title,
                description = dto.description,
                imageUrl = dto.imageUrl,
                linkUrl = dto.linkUrl,
                displayOrder = dto.displayOrder
                )
    }

    fun toDomainList(dtoList: List<CarouselItemDto>): List<CarouselItem> {
        return dtoList.map { toDomain(it) }
    }
}