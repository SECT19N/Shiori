package com.section.sho.remote

import com.section.sho.local.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenManager.tokenFlow.firstOrNull()?.accessToken }

        val request = chain.request().let { original ->
            if (accessToken.isNullOrEmpty()) {
                original
            } else {
                original.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            }
        }

        return chain.proceed(request)
    }
}
