package com.example.mobileapp.feature.homepage.domain.usecase

import com.example.mobileapp.feature.homepage.domain.model.CarouselItem
import com.example.mobileapp.feature.homepage.domain.repository.CarouselRepository
import javax.inject.Inject

class CarouselUseCase @Inject constructor(private val carouselRepository: CarouselRepository) {
    suspend operator fun invoke(): Result<List<CarouselItem>> {
        return carouselRepository.getLastestCarousel()
    }
}