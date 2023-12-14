package com.axat.geminiai.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.axat.geminiai.feature.screens.DashboardScreen


@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationRoute.DASHBOARD_SCREEN,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(NavigationRoute.DASHBOARD_SCREEN) {
            DashboardScreen(navController)
        }
    }
}