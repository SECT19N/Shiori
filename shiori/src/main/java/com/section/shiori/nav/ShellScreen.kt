package com.section.shiori.nav

sealed class ShellScreen(val route: String) {
    object Splash : ShellScreen("splash")
    object Login : ShellScreen("login")
    object MainShell : ShellScreen("main-shell")
    object Profile : ShellScreen("profile")
}