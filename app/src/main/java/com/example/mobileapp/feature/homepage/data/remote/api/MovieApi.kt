package com.example.mobileapp.feature.homepage.data.remote.api

import com.example.mobileapp.feature.homepage.data.remote.dto.MovieItemDto
import retrofit2.http.GET

interface MovieApi {
    @GET("/api/movies") suspend fun getAllShowingMovies() : List<MovieItemDto>
}