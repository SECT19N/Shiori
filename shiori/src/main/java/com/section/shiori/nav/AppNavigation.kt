package com.section.shiori.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.section.shiori.screen.mainShell.MainShellScreen
import com.section.shiori.screen.splash.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ShellScreen.Splash.route) {
        composable(ShellScreen.Splash.route) {
            SplashScreen(navController)
        }
        composable(ShellScreen.MainShell.route) {
            MainShellScreen(navController)
        }
    }
}