package com.example.mobileapp.feature.moviedetail.data.di

import com.example.mobileapp.feature.moviedetail.data.remote.api.MovieDetailApi
import com.example.mobileapp.feature.moviedetail.data.repository.MovieDetailRepositoryImpl
import com.example.mobileapp.feature.moviedetail.domain.repository.MovieDetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailModule {

    @Provides
    @Singleton
    fun provideMovieDetailApi(retrofit: Retrofit): MovieDetailApi {
        return retrofit.create(MovieDetailApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDetailRepository(movieDetailApi: MovieDetailApi): MovieDetailRepository {
        return MovieDetailRepositoryImpl(movieDetailApi)
    }
}
