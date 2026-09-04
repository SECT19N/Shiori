package com.section.sho.repository

import com.section.ori.model.Token
import com.section.ori.repository.AuthRepository
import com.section.ori.util.PkceGenerator
import com.section.sho.BuildConfig
import com.section.sho.local.TokenManager
import com.section.sho.remote.dto.toDomain
import com.section.sho.remote.service.AuthApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

private const val REDIRECT_URI = "com.section.shiori://auth-callback"

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    @Volatile
    private var pendingCodeVerifier: String? = null

    override fun observeToken(): Flow<Token?> = tokenManager.tokenFlow

    override suspend fun getAuthorizationUrl(): String {
        val codeVerifier = PkceGenerator.generateCodeVerifier()
        pendingCodeVerifier = codeVerifier

        return "https://myanimelist.net/v1/oauth2/authorize" +
                "?response_type=code" +
                "&client_id=$clientId" +
                "&redirect_uri=$REDIRECT_URI" +
                "&code_challenge=$codeVerifier" +
                "&code_challenge_method=plain"
    }

    override suspend fun exchangeAuthCode(authCode: String): Result<Token> {
        val codeVerifier = pendingCodeVerifier
            ?: return Result.failure(IllegalStateException("No login attempt in progress"))

        return try {
            val responseDto = authApiService.exchangeCodeForToken(
                clientId = clientId,
                code = authCode,
                redirectUri = REDIRECT_URI,
                codeVerifier = codeVerifier
            )
            val domainToken = responseDto.toDomain()

            tokenManager.saveToken(domainToken)
            pendingCodeVerifier = null

            Result.success(domainToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(): Result<Token> {
        return try {
            val existingRefreshToken = tokenManager.tokenFlow.firstOrNull()?.refreshToken

            if (existingRefreshToken.isNullOrEmpty()) {
                return Result.failure(IllegalStateException("No refresh token available"))
            }

            val responseDto = authApiService.refreshAccessToken(refreshToken = existingRefreshToken)
            val domainToken = responseDto.toDomain()

            tokenManager.saveToken(domainToken)

            Result.success(domainToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }

    private val clientId: String get() = BuildConfig.CLIENT_ID
}
