package com.section.ori.repository

import com.section.ori.model.Token

interface AuthRepository {
    suspend fun loginWithPkce(
        clientId: String,
        authCode: String,
        redirectUri: String,
        codeVerifier: String
    ): Result<Token>

    suspend fun getSavedToken(): Token?
}