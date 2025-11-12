package com.example.mobileapp.feature.homepage.data.remote.api

import com.example.mobileapp.feature.homepage.data.remote.dto.MembershipTierDto
import retrofit2.http.GET

interface MembershipTierApi {
    @GET("/api/membership-tiers")
    suspend fun getMembershipTiers(): List<MembershipTierDto>
}
