package com.example.mobileapp.feature.homepage.data.repository

import com.example.mobileapp.feature.homepage.data.mapper.MovieMapper
import com.example.mobileapp.feature.homepage.data.remote.api.MovieApi
import com.example.mobileapp.feature.homepage.domain.model.MovieItem
import com.example.mobileapp.feature.homepage.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : MovieRepository {
    override suspend fun getAllShowingMovie(): Result<List<MovieItem>> {
            return try {
                val response = movieApi.getAllShowingMovies()
                val movieItems = MovieMapper.toDomainList(response)
                 Result.success(movieItems)
            } catch (e : Exception) {
                Result.failure(e)
            }
    }
}