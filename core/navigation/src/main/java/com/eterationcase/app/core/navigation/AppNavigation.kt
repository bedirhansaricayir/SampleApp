package com.eterationcase.app.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.eterationcase.app.core.navigation.bottom_nav.BottomNavigationBar

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Composable
fun AppNavigationWithBottomBar(
    navController: NavHostController,
    appNavigation: @Composable (navController: NavHostController) -> Unit

) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            appNavigation(navController)
        }
    }
}