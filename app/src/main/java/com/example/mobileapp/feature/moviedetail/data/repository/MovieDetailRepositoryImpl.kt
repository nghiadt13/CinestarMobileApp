package com.example.mobileapp.feature.moviedetail.data.repository

import com.example.mobileapp.feature.moviedetail.data.mapper.MovieDetailMapper
import com.example.mobileapp.feature.moviedetail.data.remote.api.MovieDetailApi
import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail
import com.example.mobileapp.feature.moviedetail.domain.repository.MovieDetailRepository
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val movieDetailApi: MovieDetailApi
) : MovieDetailRepository {
    override suspend fun getMovieDetail(movieId: Long): Result<MovieDetail> {
        return try {
            val response = movieDetailApi.getMovieDetail(movieId)
            val movieDetail = MovieDetailMapper.toDomain(response)
            Result.success(movieDetail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
