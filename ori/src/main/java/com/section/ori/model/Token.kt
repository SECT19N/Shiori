package com.section.ori.model

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
)
