package com.section.shiori.screen.login

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section.ori.repository.AuthRepository
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

    private var lastHandledAuthCode: String? = null

    fun onSignInClicked(context: Context) {
        viewModelScope.launch {
            val authorizationUrl = authRepository.getAuthorizationUrl()
            val customTabIntent = CustomTabsIntent.Builder().build()
            customTabIntent.launchUrl(context, authorizationUrl.toUri())
        }
    }

    fun onAuthCodeReceived(authCode: String) {
        if (authCode == lastHandledAuthCode) return
        lastHandledAuthCode = authCode

        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            val result = authRepository.exchangeAuthCode(authCode)

            _loginState.value = if (result.isSuccess) {
                LoginUiState.Success
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
