package com.example.mobileapp.feature.booking.data.di

import com.example.mobileapp.feature.booking.data.remote.api.CinemaApi
import com.example.mobileapp.feature.booking.data.repository.CinemaRepositoryImpl
import com.example.mobileapp.feature.booking.domain.repository.CinemaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookingModule {

    @Provides
    @Singleton
    fun provideCinemaApi(retrofit: Retrofit): CinemaApi {
        return retrofit.create(CinemaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCinemaRepository(cinemaApi: CinemaApi): CinemaRepository {
        return CinemaRepositoryImpl(cinemaApi)
    }
}
