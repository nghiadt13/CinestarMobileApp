package com.example.mobileapp.feature.homepage.data.di

import com.example.mobileapp.feature.homepage.data.remote.api.CarouselApi
import com.example.mobileapp.feature.homepage.data.repository.CarouselRepositoryImpl
import com.example.mobileapp.feature.homepage.domain.repository.CarouselRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomePageModule {

    @Provides
    @Singleton
    fun provideCarouselApi(retrofit: Retrofit) : CarouselApi {
        return retrofit.create(CarouselApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCarouselRepository(carouselApi: CarouselApi) : CarouselRepository {
        return CarouselRepositoryImpl(carouselApi)
    }
}