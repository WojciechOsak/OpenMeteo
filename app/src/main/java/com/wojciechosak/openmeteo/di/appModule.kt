package com.wojciechosak.openmeteo.di

import com.wojciechosak.openmeteo.screen.details.DetailScreenViewModel
import com.wojciechosak.openmeteo.screen.home.HomeScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DetailScreenViewModel() } // TODO change to c-str DSL, something wrong with libs(?) TICKET-1
    viewModel { HomeScreenViewModel() }
}

