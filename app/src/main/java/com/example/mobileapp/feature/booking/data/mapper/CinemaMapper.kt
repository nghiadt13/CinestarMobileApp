package com.example.mobileapp.feature.booking.data.mapper

import com.example.mobileapp.feature.booking.data.remote.dto.CinemaDto
import com.example.mobileapp.feature.booking.domain.model.Cinema

object CinemaMapper {
    fun toDomain(dto: CinemaDto): Cinema {
        return Cinema(
            id = dto.id,
            name = dto.name,
            address = dto.address,
            city = dto.city,
            district = dto.district,
            phoneNumber = dto.phoneNumber,
            latitude = dto.latitude,
            longitude = dto.longitude
        )
    }

    fun toDomainList(dtoList: List<CinemaDto>): List<Cinema> {
        return dtoList.map { toDomain(it) }
    }
}
