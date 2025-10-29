package com.example.mobileapp.feature.homepage.ui.adapter

import com.example.mobileapp.feature.homepage.domain.model.CarouselItem
import com.example.mobileapp.feature.homepage.domain.model.MovieItem

sealed interface HomeItem {
    data class CarouselSection(val items: List<CarouselItem>) : HomeItem

    data class MovieListSection(val items : List<MovieItem>) : HomeItem
}
