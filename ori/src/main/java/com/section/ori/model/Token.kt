package com.section.ori.model

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
)

fun Token.isExpired(skewMillis: Long = 60_000): Boolean {
    return System.currentTimeMillis() + skewMillis >= expiresAt
}
