package com.example.mobileapp.core.network

import com.example.mobileapp.core.security.TokenManager
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = tokenManager.getToken()

        val newRequest =
                if (token != null) {
                    originalRequest.newBuilder().header("Authorization", "Bearer $token").build()
                } else {
                    originalRequest
                }

        return chain.proceed(newRequest)
    }
}
