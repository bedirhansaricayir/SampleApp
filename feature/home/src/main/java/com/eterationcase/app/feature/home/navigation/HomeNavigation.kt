package com.eterationcase.app.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.eterationcase.app.core.navigation.screens.AppScreen
import com.eterationcase.app.feature.home.HomeScreen

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

val HOME_SCREEN = AppScreen.Home

fun NavController.navigateToHome() = navigate(HOME_SCREEN)

fun NavGraphBuilder.homeScreen(
    navigateToDetail: (productId: String) -> Unit
) {
    composable<AppScreen.Home> {
        HomeScreen(
            navigateToDetail = navigateToDetail
        )
    }
}