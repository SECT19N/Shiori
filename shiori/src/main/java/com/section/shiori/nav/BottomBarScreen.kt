package com.section.shiori.nav

import androidx.compose.ui.graphics.vector.ImageVector
import com.section.shiori.ui.icons.explore
import com.section.shiori.ui.icons.exploreSelected
import com.section.shiori.ui.icons.home
import com.section.shiori.ui.icons.homeSelected
import com.section.shiori.ui.icons.list
import com.section.shiori.ui.icons.listSelected

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val iconSelected: ImageVector
) {
    object Home : BottomBarScreen("home", "Home", home, homeSelected)
    object Discover : BottomBarScreen("discover", "Discover", explore, exploreSelected)
    object MyList : BottomBarScreen("myList", "My List", list, listSelected)
}