package com.example.mobileapp.feature.booking.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.booking.domain.model.Cinema
import com.example.mobileapp.feature.booking.domain.usecase.GetCinemasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel @Inject constructor(
    private val getCinemasUseCase: GetCinemasUseCase
) : ViewModel() {

    private val _cinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val cinemas: StateFlow<List<Cinema>> = _cinemas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchCinemasByMovieId(movieId: Long, city: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getCinemasUseCase(movieId, city)
                .onSuccess { cinemaList ->
                    Log.d("CinemaViewModel", "Fetched ${cinemaList.size} cinemas for movie $movieId")
                    _cinemas.value = cinemaList
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    Log.e("CinemaViewModel", "Error fetching cinemas", exception)
                    _error.value = exception.message ?: "Unknown error"
                    _isLoading.value = false
                }
        }
    }
}
