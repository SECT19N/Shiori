package com.section.shiori.nav

sealed class ShellScreen(val route: String) {
    object Splash : ShellScreen("splash")
    object MainShell : ShellScreen("mainShell")
}