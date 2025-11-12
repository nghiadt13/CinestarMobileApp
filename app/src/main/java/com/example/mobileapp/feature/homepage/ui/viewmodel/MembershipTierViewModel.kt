package com.example.mobileapp.feature.homepage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.homepage.domain.model.MembershipTierItem
import com.example.mobileapp.feature.homepage.domain.usecase.MembershipTierUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembershipTierViewModel @Inject constructor(
    private val membershipTierUseCase: MembershipTierUseCase
) : ViewModel() {

    private val _membershipTiers = MutableStateFlow<List<MembershipTierItem>>(emptyList())
    val membershipTiers: StateFlow<List<MembershipTierItem>> = _membershipTiers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow<String?>(null)
    val isError: StateFlow<String?> = _isError.asStateFlow()

    init {
        fetchMembershipTiers()
    }

    fun fetchMembershipTiers() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = null

            membershipTierUseCase()
                .onSuccess { items ->
                    Log.d("MembershipTierViewModel", "Fetched ${items.size} membership tiers")
                    _membershipTiers.value = items
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    Log.e("MembershipTierViewModel", "Fetching error", exception)
                    _isError.value = exception.message ?: "Unknown Error"
                    _isLoading.value = false
                }
        }
    }
}
