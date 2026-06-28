package com.section.sho.remote.dto

import com.section.ori.model.Token
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenDto(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String?,
    @Json(name = "expires_in") val expiresIn: Long
)

fun TokenDto.toDomain(): Token {
    return Token(
        accessToken = accessToken,
        refreshToken = refreshToken ?: "",
        expiresAt = System.currentTimeMillis() + (expiresIn * 1000)
    )
}