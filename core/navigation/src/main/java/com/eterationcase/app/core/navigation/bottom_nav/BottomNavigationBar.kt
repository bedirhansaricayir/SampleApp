package com.eterationcase.app.core.navigation.bottom_nav

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.eterationcase.app.core.navigation.screens.NavBarScreen

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Composable
internal fun BottomNavigationBar(
    navController: NavHostController,
    badgeCount: Int? = null,
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        BottomNavigationItem.entries.forEachIndexed { index, item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true

            NavigationBarItem(
                selected = isSelected,
                label = { Text(text = item.title) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.route == NavBarScreen.Basket) {
                                if (badgeCount != null) {
                                    Badge {
                                        Text(text = "$badgeCount")
                                    }
                                }
                            }
                        }
                    )
                    {
                        Icon(
                            painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                            contentDescription = item.title,
                        )
                    }

                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}