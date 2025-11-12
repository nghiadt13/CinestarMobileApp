package com.example.mobileapp.feature.homepage.data.di

import com.example.mobileapp.feature.homepage.data.remote.api.CarouselApi
import com.example.mobileapp.feature.homepage.data.remote.api.MovieApi
import com.example.mobileapp.feature.homepage.data.remote.api.NewsApi
import com.example.mobileapp.feature.homepage.data.repository.CarouselRepositoryImpl
import com.example.mobileapp.feature.homepage.data.repository.MovieRepositoryImpl
import com.example.mobileapp.feature.homepage.data.repository.NewsRepositoryImpl
import com.example.mobileapp.feature.homepage.domain.repository.CarouselRepository
import com.example.mobileapp.feature.homepage.domain.repository.MovieRepository
import com.example.mobileapp.feature.homepage.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object HomePageModule {

    @Provides
    @Singleton
    fun provideCarouselApi(retrofit: Retrofit): CarouselApi {
        return retrofit.create(CarouselApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(movieApi: MovieApi): MovieRepository {
        return MovieRepositoryImpl(movieApi)
    }

    @Provides
    @Singleton
    fun provideCarouselRepository(carouselApi: CarouselApi): CarouselRepository {
        return CarouselRepositoryImpl(carouselApi)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApi: NewsApi): NewsRepository {
        return NewsRepositoryImpl(newsApi)
    }
}

