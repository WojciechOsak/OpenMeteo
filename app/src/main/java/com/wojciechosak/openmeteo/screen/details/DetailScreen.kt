package com.wojciechosak.openmeteo.screen.details

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailScreen(city: String?) {
    val viewModel = koinViewModel<DetailScreenViewModel>()

}