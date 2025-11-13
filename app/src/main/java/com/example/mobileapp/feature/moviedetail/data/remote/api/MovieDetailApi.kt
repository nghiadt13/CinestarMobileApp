package com.example.mobileapp.feature.moviedetail.data.remote.api

import com.example.mobileapp.feature.moviedetail.data.remote.dto.MovieDetailDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailApi {
    @GET("/api/movies/{id}")
    suspend fun getMovieDetail(@Path("id") movieId: Long): MovieDetailDto
}
