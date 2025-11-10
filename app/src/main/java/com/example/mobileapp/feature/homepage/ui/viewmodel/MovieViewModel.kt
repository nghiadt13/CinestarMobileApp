package com.example.mobileapp.feature.homepage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.homepage.domain.model.MovieItem
import com.example.mobileapp.feature.homepage.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {
    private  val _movieItem = MutableStateFlow<List<MovieItem>>(emptyList())
    val movieItem : StateFlow<List<MovieItem>> = _movieItem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<String?>(null)
    val isError : StateFlow<String? > = _isError.asStateFlow()

    init {
        fetchMovieList()
    }

    fun fetchMovieList() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = null

            movieUseCase()
                .onSuccess {
                    items ->
                    Log.d("Movie View Model","Fetching ${items.size} Movie")
                    _movieItem.value = items
                    _isLoading.value = false
                }
                .onFailure {
                    exception ->
                    Log.e("Movie View Model","Fetching error",exception)
                    _isError.value = exception.message?: "Unknown Error"
                    _isLoading.value = false
                }
        }
    }
}