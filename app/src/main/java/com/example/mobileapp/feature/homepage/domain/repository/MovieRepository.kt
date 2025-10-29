package com.example.mobileapp.feature.homepage.domain.repository

import com.example.mobileapp.feature.homepage.domain.model.MovieItem

interface MovieRepository {
    suspend fun getAllShowingMovie() : Result<List<MovieItem>>
}