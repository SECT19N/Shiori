package com.section.ori.util

import java.security.SecureRandom

object PkceGenerator {
    private const val VERIFIER_BYTE_LENGTH = 64
    private val secureRandom = SecureRandom()

    fun generateCodeVerifier(): String {
        val bytes = ByteArray(VERIFIER_BYTE_LENGTH)
        secureRandom.nextBytes(bytes)
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}
