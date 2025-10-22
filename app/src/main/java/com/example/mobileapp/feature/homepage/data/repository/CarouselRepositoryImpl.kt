package com.example.mobileapp.feature.homepage.data.repository

import com.example.mobileapp.feature.homepage.data.mapper.CarouselMapper
import com.example.mobileapp.feature.homepage.data.remote.api.CarouselApi
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem
import com.example.mobileapp.feature.homepage.domain.repository.CarouselRepository
import javax.inject.Inject

class CarouselRepositoryImpl @Inject constructor(private val carouselApi: CarouselApi) :
        CarouselRepository {

    override suspend fun getLastestCarousel(): Result<List<CarouselItem>> {
        return try {
            val response = carouselApi.getLastestCarousel()

            if (response.success && response.data.isNotEmpty()) {
                val carouselItems = CarouselMapper.toDomainList(response.data)
                Result.success(carouselItems)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
