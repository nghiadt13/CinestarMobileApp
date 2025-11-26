package com.example.mobileapp.feature.booking.domain.model

import java.math.BigDecimal

data class Cinema(
        val id: Long,
        val name: String,
        val address: String,
        val city: String,
        val district: String? = null,
        val phoneNumber: String? = null,
        val email: String? = null,
        val latitude: BigDecimal? = null,
        val longitude: BigDecimal? = null,
        val logoUrl: String = "",
        val distance: String? = null,
        val has2D: Boolean = true,
        val hasSubtitle: Boolean = true
)
