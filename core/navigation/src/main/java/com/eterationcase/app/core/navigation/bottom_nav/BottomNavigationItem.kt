package com.eterationcase.app.core.navigation.bottom_nav

import androidx.compose.ui.graphics.vector.ImageVector
import com.eterationcase.app.core.navigation.R
import com.eterationcase.app.core.navigation.screens.NavBarScreen

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

internal enum class BottomNavigationItem(
    val route: NavBarScreen,
    val title: String,
    val unselectedIcon: Int,
    val selectedIcon: Int
) {
    HOME(
        NavBarScreen.Home,
        "Home",
        R.drawable.icon_home,
        R.drawable.icon_home
    ),
    BASKET(
        NavBarScreen.Basket,
        "Basket",
        R.drawable.icon_basket,
        R.drawable.icon_basket
    ),
    FAVORITE(
        NavBarScreen.Favorite,
        "Favorite",
        R.drawable.icon_favorite,
        R.drawable.icon_favorite
    ),
    PROFILE(
        NavBarScreen.Profile,
        "Profile",
        R.drawable.icon_profile,
        R.drawable.icon_profile
    )
}