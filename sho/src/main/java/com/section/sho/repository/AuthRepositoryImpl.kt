package com.section.sho.repository

import com.section.ori.model.Token
import com.section.ori.repository.AuthRepository
import com.section.sho.local.TokenManager
import com.section.sho.remote.dto.toDomain
import com.section.sho.remote.service.AuthApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun loginWithPkce(
        clientId: String,
        authCode: String,
        redirectUri: String,
        codeVerifier: String
    ): Result<Token> {
        return try {
            val responseDto = authApiService.exchangeCodeForToken(
                clientId = clientId,
                code = authCode,
                redirectUri = redirectUri,
                codeVerifier = codeVerifier
            )
            val domainToken = responseDto.toDomain()

            tokenManager.saveToken(domainToken)

            Result.success(domainToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSavedToken(): Token? {
        return tokenManager.tokenFlow.first()
    }
}