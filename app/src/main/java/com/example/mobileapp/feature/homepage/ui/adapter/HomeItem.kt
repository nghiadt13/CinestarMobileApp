package com.example.mobileapp.feature.homepage.ui.adapter

import com.example.mobileapp.feature.homepage.domain.model.CarouselItem

sealed interface HomeItem {
    data class CarouselSection(val item: CarouselItem) : HomeItem
}