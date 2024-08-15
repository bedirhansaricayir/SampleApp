package com.eterationcase.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.eterationcase.app.core.navigation.AppNavigation
import com.eterationcase.app.core.navigation.screens.AppScreen
import com.eterationcase.app.core.navigation.screens.NavBarScreen
import com.eterationcase.app.feature.cart.navigation.cartScreen
import com.eterationcase.app.feature.detail.navigation.detailScreen
import com.eterationcase.app.feature.detail.navigation.navigateToDetail
import com.eterationcase.app.feature.home.navigation.homeScreen

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    AppNavigation(navController = navController) {
        NavHost(navController = navController,
            startDestination = NavBarScreen.Home
        ) {
            homeRoot(navController)
            basketRoot(navController)
        }
    }
}

private fun NavGraphBuilder.homeRoot(navController: NavController) {
    navigation<NavBarScreen.Home>(
        startDestination = AppScreen.Home
    ) {
        homeScreen(
            navigateToDetail = navController::navigateToDetail
        )

        detailScreen(
            onNavigateBack = navController::popBackStack
        )
    }
}

private fun NavGraphBuilder.basketRoot(navController: NavController) {
    navigation<NavBarScreen.Basket>(
        startDestination = AppScreen.Basket
    ) {
        cartScreen()
    }
}