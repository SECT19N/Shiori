package com.section.ori.repository

import com.section.ori.model.Token
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeToken(): Flow<Token?>

    suspend fun getAuthorizationUrl(): String

    suspend fun exchangeAuthCode(authCode: String): Result<Token>

    suspend fun refreshToken(): Result<Token>

    suspend fun logout()
}
