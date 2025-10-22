package com.example.mobileapp.feature.homepage.domain.repository

import com.example.mobileapp.feature.homepage.domain.model.CarouselItem
import javax.inject.Inject

interface CarouselRepository {
    suspend fun getLastestCarousel() : Result<List<CarouselItem>>
}