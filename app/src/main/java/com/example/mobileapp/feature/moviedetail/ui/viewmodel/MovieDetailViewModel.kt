package com.example.mobileapp.feature.moviedetail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail
import com.example.mobileapp.feature.moviedetail.domain.usecase.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchMovieDetail(movieId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getMovieDetailUseCase(movieId)
                .onSuccess { detail ->
                    _movieDetail.value = detail
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Unknown Error"
                    _isLoading.value = false
                }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
