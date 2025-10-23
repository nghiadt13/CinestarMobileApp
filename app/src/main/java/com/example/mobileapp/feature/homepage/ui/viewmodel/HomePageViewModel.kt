package com.example.mobileapp.feature.homepage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem
import com.example.mobileapp.feature.homepage.domain.usecase.CarouselUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomePageViewModel @Inject constructor(private val carouselUseCase: CarouselUseCase) :
        ViewModel() {
    private val _carouselItem = MutableStateFlow<List<CarouselItem>>(emptyList())
    val carouselItem: StateFlow<List<CarouselItem>> = _carouselItem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchCarouselItem()
    }

    private fun fetchCarouselItem() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            Log.d("HomePageViewModel", "Fetching carousel items...")

            carouselUseCase()
                    .onSuccess { items ->
                        Log.d("HomePageViewModel", "Success: Received ${items.size} items")
                        _carouselItem.value = items
                    }
                    .onFailure { exception ->
                        Log.e("HomePageViewModel", "Error fetching carousel", exception)
                        _error.value = exception.message ?: "Unknown error"
                    }

            _isLoading.value = false
        }
    }
}
