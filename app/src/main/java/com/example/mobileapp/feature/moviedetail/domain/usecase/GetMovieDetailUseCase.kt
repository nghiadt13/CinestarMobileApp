package com.example.mobileapp.feature.moviedetail.domain.usecase

import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail
import com.example.mobileapp.feature.moviedetail.domain.repository.MovieDetailRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val movieDetailRepository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: Long): Result<MovieDetail> {
        return movieDetailRepository.getMovieDetail(movieId)
    }
}
