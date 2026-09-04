package com.section.shiori.nav

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.section.shiori.screen.login.LoginScreen
import com.section.shiori.screen.login.LoginScreenViewModel
import com.section.shiori.screen.mainShell.MainShellScreen
import com.section.shiori.screen.profile.ProfileScreen
import com.section.shiori.screen.splash.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController, pendingAuthCode: String?) {
    NavHost(navController = navController, startDestination = ShellScreen.Splash.route) {
        composable(ShellScreen.Splash.route) {
            SplashScreen(navController)
        }
        composable(ShellScreen.Login.route) {
            val loginScreenViewModel: LoginScreenViewModel = hiltViewModel()

            LoginScreen(
                viewModel = loginScreenViewModel,
                navController = navController,
                pendingAuthCode = pendingAuthCode
            )
        }
        composable(ShellScreen.MainShell.route) {
            MainShellScreen(navController)
        }
        composable(ShellScreen.Profile.route) {
            ProfileScreen(navController)
        }
    }
}
