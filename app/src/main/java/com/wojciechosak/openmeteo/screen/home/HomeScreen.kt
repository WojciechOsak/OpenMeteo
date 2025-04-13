package com.wojciechosak.openmeteo.screen.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    Text("HomeScreen")
}