package com.wojciechosak.openmeteo.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wojciechosak.openmeteo.screen.Screen
import com.wojciechosak.openmeteo.screen.details.DetailScreen
import com.wojciechosak.openmeteo.screen.home.HomeScreen
import com.wojciechosak.openmeteo.screen.permission.PermissionScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PermissionScreen.route,
        modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        composable(route = Screen.PermissionScreen.route) {
            PermissionScreen(navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route + "?text={text}",
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            DetailScreen(city = it.arguments?.getString("city"))
        }
    }
}