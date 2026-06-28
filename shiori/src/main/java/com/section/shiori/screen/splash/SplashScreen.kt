package com.section.shiori.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.section.shiori.nav.ShellScreen
import com.section.sho.local.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Duration.Companion.seconds

@Composable
fun SplashScreen(navController: NavController, tokenManager: TokenManager) {
    LaunchedEffect(Unit) {
        val currentToken = tokenManager.tokenFlow.firstOrNull()

        delay(1.5.seconds)

        val targetRoute = if (currentToken != null) {
            ShellScreen.MainShell.route
        } else {
            ShellScreen.Login.route
        }

        navController.navigate(targetRoute) {
            popUpTo(ShellScreen.Splash.route) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to Shiori",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}