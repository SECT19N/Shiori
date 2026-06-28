package com.section.shiori.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.section.shiori.screen.login.LoginScreen
import com.section.shiori.screen.login.LoginScreenViewModel
import com.section.shiori.screen.mainShell.MainShellScreen
import com.section.shiori.screen.splash.SplashScreen
import com.section.sho.local.TokenManager

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = ShellScreen.Splash.route) {
        composable(ShellScreen.Splash.route) {
            SplashScreen(
                navController,
                TokenManager(context)
            )
        }
        composable(ShellScreen.Login.route) {
            val loginScreenViewModel: LoginScreenViewModel = hiltViewModel()

            LoginScreen(viewModel = loginScreenViewModel)
        }
        composable(ShellScreen.MainShell.route) {
            MainShellScreen(navController)
        }
    }
}