package com.example.mobileapp.feature.homepage.domain.repository

import com.example.mobileapp.feature.homepage.domain.model.MembershipTierItem

interface MembershipTierRepository {
    suspend fun getMembershipTiers(): Result<List<MembershipTierItem>>
}
