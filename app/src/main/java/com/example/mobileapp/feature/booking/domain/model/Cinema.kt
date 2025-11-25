package com.example.mobileapp.feature.booking.domain.model

import java.math.BigDecimal

data class Cinema(
    val id: Long,
    val name: String,
    val address: String,
    val city: String,
    val district: String?,
    val phoneNumber: String?,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?
) {
    val fullAddress: String
        get() = if (district != null) {
            "$address, $district, $city"
        } else {
            "$address, $city"
        }

    val hasLocation: Boolean
        get() = latitude != null && longitude != null
}
