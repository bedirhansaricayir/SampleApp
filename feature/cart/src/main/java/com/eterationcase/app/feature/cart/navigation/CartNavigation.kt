package com.eterationcase.app.feature.cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.eterationcase.app.core.navigation.screens.AppScreen
import com.eterationcase.app.feature.cart.CartScreen

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

val CART_SCREEN = AppScreen.Basket

fun NavController.navigateToCart() = navigate(CART_SCREEN)

fun NavGraphBuilder.cartScreen() {
    composable<AppScreen.Basket> {
        CartScreen()
    }
}