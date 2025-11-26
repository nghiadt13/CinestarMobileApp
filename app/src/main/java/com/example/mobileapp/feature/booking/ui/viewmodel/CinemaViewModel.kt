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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel @Inject constructor(
    private val getCinemasUseCase: GetCinemasUseCase
) : ViewModel() {

    private val _allCinemas = MutableStateFlow<List<Cinema>>(emptyList())

    private val _filteredCinemas = MutableStateFlow<List<Cinema>>(emptyList())
    val filteredCinemas: StateFlow<List<Cinema>> = _filteredCinemas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentCity = MutableStateFlow("Hà Nội")
    val currentCity: StateFlow<String> = _currentCity.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private val _availableCities = MutableStateFlow<List<String>>(emptyList())
    val availableCities: StateFlow<List<String>> = _availableCities.asStateFlow()

    init {
        viewModelScope.launch {
            combine(_allCinemas, _currentCity, _searchQuery) { cinemas, city, query ->
                filterCinemas(cinemas, city, query)
            }.collect { filtered ->
                _filteredCinemas.value = filtered
            }
        }
    }

    fun fetchCinemasByMovieId(movieId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getCinemasUseCase(movieId, null)
                .onSuccess { cinemaList ->
                    Log.d("CinemaViewModel", "Fetched ${cinemaList.size} cinemas for movie $movieId")
                    _allCinemas.value = cinemaList

                    // Extract available cities
                    val cities = cinemaList.map { it.city }.distinct().sorted()
                    _availableCities.value = cities

                    // Set default city if current city is not available
                    if (cities.isNotEmpty() && !cities.contains(_currentCity.value)) {
                        _currentCity.value = cities.first()
                    }

                    _isLoading.value = false
                }
                .onFailure { exception ->
                    Log.e("CinemaViewModel", "Error fetching cinemas", exception)
                    _error.value = exception.message ?: "Có lỗi xảy ra khi tải danh sách rạp"
                    _isLoading.value = false
                }
        }
    }

    fun setCity(city: String) {
        _currentCity.value = city
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun filterCinemas(cinemas: List<Cinema>, city: String, query: String): List<Cinema> {
        val filteredByCity = cinemas.filter { it.city == city }

        return if (query.isBlank()) {
            filteredByCity
        } else {
            filteredByCity.filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.address.contains(query, ignoreCase = true)
            }
        }
    }
}
