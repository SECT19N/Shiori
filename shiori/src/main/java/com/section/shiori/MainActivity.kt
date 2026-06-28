package com.section.shiori

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.section.shiori.nav.AppNavigation
import com.section.shiori.screen.login.LoginScreenViewModel
import com.section.shiori.ui.theme.ShioriTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginScreenViewModel: LoginScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            ShioriTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data: Uri? = intent.data

        if (data != null && data.scheme == "com.section.shiori" && data.host == "auth-callback") {
            val authCode = data.getQueryParameter("code")
            if (authCode != null) {
                loginScreenViewModel.exchangeCode(authCode)
            }
        }
    }
}