package com.example.mobileapp.feature.homepage.data.mapper

import com.example.mobileapp.feature.homepage.data.remote.dto.MembershipTierDto
import com.example.mobileapp.feature.homepage.domain.model.MembershipTierItem

object MembershipTierMapper {
    fun toDomainMapper(dto: MembershipTierDto): MembershipTierItem {
        return MembershipTierItem(
            id = dto.id,
            name = dto.name,
            imageUrl = dto.imageUrl,
            description = dto.description
        )
    }

    fun toDomainList(dtoList: List<MembershipTierDto>): List<MembershipTierItem> {
        return dtoList.map {
            toDomainMapper(it)
        }
    }
}
