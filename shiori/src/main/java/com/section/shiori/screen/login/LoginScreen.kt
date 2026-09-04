package com.section.shiori.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.section.shiori.nav.ShellScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel,
    navController: NavController,
    pendingAuthCode: String?,
) {
    val state by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(pendingAuthCode) {
        pendingAuthCode?.let { viewModel.onAuthCodeReceived(it) }
    }

    LaunchedEffect(state) {
        if (state is LoginUiState.Success) {
            navController.navigate(ShellScreen.MainShell.route) {
                popUpTo(ShellScreen.Login.route) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Track Your Journey",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sign in with your MyAnimeList account to sync your collection securely.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        when (val currentState = state) {
            is LoginUiState.Loading -> {
                LoadingIndicator()
            }

            is LoginUiState.Error -> {
                Text(
                    text = currentState.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.onSignInClicked(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Try Again")
                }
            }

            else -> {
                Button(
                    onClick = { viewModel.onSignInClicked(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign In with MyAnimeList")
                }
            }
        }
    }
}
