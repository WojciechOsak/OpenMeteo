package com.wojciechosak.openmeteo.navigation

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

@Composable
fun NavigationStack(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier) {
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