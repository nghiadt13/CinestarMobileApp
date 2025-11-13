package com.example.mobileapp.feature.moviedetail.domain.repository

import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: Long): Result<MovieDetail>
}
