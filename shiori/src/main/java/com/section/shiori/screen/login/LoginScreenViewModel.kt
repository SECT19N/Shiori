package com.section.shiori.screen.login

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section.ori.repository.AuthRepository
import com.section.shiori.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun onLaunchCustomTab(url: String, context: Context) {
        val customTabIntent = CustomTabsIntent.Builder().build()
        customTabIntent.launchUrl(context, url.toUri())
    }

    fun getMalAuthorizationUrl(): String {
        val clientId = BuildConfig.CLIENT_ID
        val redirectUri = "com.section.shiori://auth-callback"

        return "https://myanimelist.net/v1/oauth2/authorize" +
                "?response_type=code" +
                "&client_id=$clientId" +
                "&redirect_uri=$redirectUri" +
                "&code_challenge=${BuildConfig.CODE_VERIFIER}" +
                "&code_challenge_method=plain"
    }

    fun exchangeCode(authCode: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            val result = authRepository.loginWithPkce(
                clientId = BuildConfig.CLIENT_ID,
                authCode = authCode,
                redirectUri = "com.section.shiori://auth-callback",
                codeVerifier = BuildConfig.CODE_VERIFIER
            )

            if (result.isSuccess) {
                _loginState.value = LoginUiState.Success
            } else {
                _loginState.value =
                    LoginUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}