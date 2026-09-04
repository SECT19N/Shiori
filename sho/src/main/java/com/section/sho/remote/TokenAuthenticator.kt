package com.section.sho.remote

import com.section.ori.repository.AuthRepository
import com.section.sho.local.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_RETRY_COUNT = 1

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : Authenticator {
    private val refreshMutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) > MAX_RETRY_COUNT) {
            return null
        }

        val failedAccessToken = runBlocking { tokenManager.tokenFlow.firstOrNull()?.accessToken }

        val refreshedAccessToken = runBlocking {
            refreshMutex.withLock {
                val currentAccessToken = tokenManager.tokenFlow.firstOrNull()?.accessToken
                if (currentAccessToken != null && currentAccessToken != failedAccessToken) {
                    currentAccessToken
                } else {
                    authRepository.refreshToken().getOrNull()?.accessToken
                }
            }
        } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $refreshedAccessToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }
}
