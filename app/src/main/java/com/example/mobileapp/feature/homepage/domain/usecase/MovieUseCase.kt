package com.example.mobileapp.feature.homepage.domain.usecase

import com.example.mobileapp.feature.homepage.domain.model.MovieItem
import com.example.mobileapp.feature.homepage.domain.repository.MovieRepository
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val movieRepository: MovieRepository)  {
    suspend fun invoke() : Result<List<MovieItem>> {
        return movieRepository.getAllShowingMovie()
    }
}