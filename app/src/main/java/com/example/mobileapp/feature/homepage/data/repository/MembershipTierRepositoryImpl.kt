package com.example.mobileapp.feature.homepage.data.repository

import com.example.mobileapp.feature.homepage.data.mapper.MembershipTierMapper
import com.example.mobileapp.feature.homepage.data.remote.api.MembershipTierApi
import com.example.mobileapp.feature.homepage.domain.model.MembershipTierItem
import com.example.mobileapp.feature.homepage.domain.repository.MembershipTierRepository
import javax.inject.Inject

class MembershipTierRepositoryImpl @Inject constructor(
    private val membershipTierApi: MembershipTierApi
) : MembershipTierRepository {
    override suspend fun getMembershipTiers(): Result<List<MembershipTierItem>> {
        return try {
            val response = membershipTierApi.getMembershipTiers()
            val membershipTiers = MembershipTierMapper.toDomainList(response)
            Result.success(membershipTiers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
