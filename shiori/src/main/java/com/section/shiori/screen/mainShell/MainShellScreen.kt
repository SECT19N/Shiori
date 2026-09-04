package com.section.shiori.screen.mainShell

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.section.shiori.nav.BottomBarScreen
import com.section.shiori.screen.myList.MyListScreen

@Composable
fun MainShellScreen(rootNavController: NavController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) {
                Text("hi")
            }
            composable(BottomBarScreen.Discover.route) {
                Text("hi1")
            }
            composable(BottomBarScreen.Seasons.route) {
                Text("seasons")
            }
            composable(BottomBarScreen.Search.route) {
                Text("search")
            }
            composable(BottomBarScreen.MyList.route) {
                MyListScreen(rootNavController = rootNavController)
            }
        }
    }
}
