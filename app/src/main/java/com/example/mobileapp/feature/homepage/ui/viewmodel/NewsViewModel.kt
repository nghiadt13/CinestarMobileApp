package com.example.mobileapp.feature.homepage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.homepage.domain.model.NewsItem
import com.example.mobileapp.feature.homepage.domain.usecase.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsUseCase: NewsUseCase
) : ViewModel() {

    private val _newsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsItems: StateFlow<List<NewsItem>> = _newsItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<String?>(null)
    val isError: StateFlow<String?> = _isError.asStateFlow()

    init {
        fetchNewsList()
    }

    fun fetchNewsList() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = null

            newsUseCase()
                .onSuccess { items ->
                    Log.d("NewsViewModel", "Fetched ${items.size} news items")
                    _newsItems.value = items
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    Log.e("NewsViewModel", "Fetching error", exception)
                    _isError.value = exception.message ?: "Unknown Error"
                    _isLoading.value = false
                }
        }
    }
}
