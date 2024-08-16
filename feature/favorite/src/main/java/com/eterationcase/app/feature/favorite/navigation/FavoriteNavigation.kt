package com.eterationcase.app.feature.favorite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.eterationcase.app.core.navigation.screens.AppScreen
import com.eterationcase.app.feature.favorite.FavoriteScreen

/**
 * Created by bedirhansaricayir on 16.08.2024
 */

val FAVORITE_SCREEN = AppScreen.Favorite

fun NavController.navigateToFavorite() = navigate(FAVORITE_SCREEN)

fun NavGraphBuilder.favoriteScreen() {
    composable<AppScreen.Favorite> {
        FavoriteScreen()
    }
}