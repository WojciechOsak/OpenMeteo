package com.wojciechosak.openmeteo.screen

sealed class Screen(val route: String) {
    data object PermissionScreen: Screen("permission_screen")
    data object Home: Screen("home_screen")
    data object Detail: Screen("detail_screen")
}