package com.wojciechosak.openmeteo.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        composable(route = Screen.PermissionScreen.route) {
            PermissionScreen(navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route + "/{$LOCATION_ARG}/{$LAT_ARG}/{$LON_ARG}",
            arguments = listOf(
                navArgument(LOCATION_ARG) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(LAT_ARG) {
                    type = NavType.FloatType
                    nullable = false
                },
                navArgument(LON_ARG) {
                    type = NavType.FloatType
                    nullable = false
                }
            )
        ) {
            DetailScreen(
                location = it.arguments?.getString(LOCATION_ARG)!!,
                lat = it.arguments?.getFloat(LAT_ARG)!!,
                lon = it.arguments?.getFloat(LON_ARG)!!
            )
        }
    }
}
private const val LOCATION_ARG = "location"
private const val LAT_ARG = "lat"
private const val LON_ARG = "lon"