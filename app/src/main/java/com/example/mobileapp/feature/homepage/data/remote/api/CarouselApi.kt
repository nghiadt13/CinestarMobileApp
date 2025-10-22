package com.example.mobileapp.feature.homepage.data.remote.api

import com.example.mobileapp.feature.homepage.data.remote.dto.CarouselResponseDto
import retrofit2.http.GET

interface CarouselApi {
    @GET("/api/carousel/latest") suspend fun getLastestCarousel(): CarouselResponseDto
}
