package com.section.shiori.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section.ori.model.isExpired
import com.section.ori.repository.AuthRepository
import com.section.shiori.nav.ShellScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val currentToken = authRepository.observeToken().firstOrNull()

            val isSignedIn = when {
                currentToken == null -> false
                !currentToken.isExpired() -> true
                else -> authRepository.refreshToken().isSuccess
            }

            _startDestination.value = if (isSignedIn) {
                ShellScreen.MainShell.route
            } else {
                ShellScreen.Login.route
            }
        }
    }
}
