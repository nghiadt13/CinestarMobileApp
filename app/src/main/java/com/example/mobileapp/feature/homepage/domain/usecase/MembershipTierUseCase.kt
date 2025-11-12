package com.example.mobileapp.feature.homepage.domain.usecase

import com.example.mobileapp.feature.homepage.domain.model.MembershipTierItem
import com.example.mobileapp.feature.homepage.domain.repository.MembershipTierRepository
import javax.inject.Inject

class MembershipTierUseCase @Inject constructor(
    private val membershipTierRepository: MembershipTierRepository
) {
    suspend operator fun invoke(): Result<List<MembershipTierItem>> {
        return membershipTierRepository.getMembershipTiers()
    }
}
