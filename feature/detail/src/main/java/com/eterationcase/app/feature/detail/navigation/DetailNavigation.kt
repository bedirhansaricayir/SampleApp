package com.eterationcase.app.feature.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.eterationcase.app.core.navigation.screens.AppScreen
import com.eterationcase.app.feature.detail.DetailScreen

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

fun NavController.navigateToDetail(productId: String) = navigate(AppScreen.Detail(productId = productId))

fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit
) {
    composable<AppScreen.Detail> {
        val args = it.toRoute<AppScreen.Detail>()
        DetailScreen(
            productId = args.productId,
            onNavigateBack = onNavigateBack
        )
    }
}