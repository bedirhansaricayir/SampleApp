package com.eterationcase.app.core.navigation.screens

import kotlinx.serialization.Serializable
import java.io.Serial

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Serializable
sealed class NavBarScreen {
    @Serializable data object Home : NavBarScreen()
    @Serializable data object Basket: NavBarScreen()
    @Serializable data object Favorite: NavBarScreen()
    @Serializable data object Profile: NavBarScreen()
}

@Serializable
sealed class AppScreen {
    @Serializable data object HomeTab : AppScreen()
    @Serializable data object BasketTab: AppScreen()
    @Serializable data object FavoriteTab: AppScreen()
    @Serializable data object ProfileTab: AppScreen()

    //<!-- region Sub-Dashboard -->
    @Serializable data object Home : AppScreen()
    @Serializable data class Detail(val productId: String) : AppScreen()
    //<!-- endregion -->

    //<!-- region Sub-Basket -->
    @Serializable data object Basket : AppScreen()
    //<!-- endregion -->

    //<!-- region Sub-Favorite -->
    @Serializable data object Favorite : AppScreen()
    //<!-- endregion -->

    //<!-- region Sub-Profile -->
    @Serializable data object Profile : AppScreen()
    //<!-- endregion -->


}