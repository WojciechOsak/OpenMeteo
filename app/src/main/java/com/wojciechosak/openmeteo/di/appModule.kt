package com.wojciechosak.openmeteo.di

import com.wojciechosak.openmeteo.core.DispatcherProvider
import com.wojciechosak.openmeteo.screen.details.DetailScreenViewModel
import com.wojciechosak.openmeteo.screen.home.HomeScreenViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DetailScreenViewModel() } // TODO change to c-str DSL, something wrong with libs(?) TICKET-1
    viewModel { HomeScreenViewModel(weatherRepository = get()) }
    single<DispatcherProvider> {
        object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default

        }
    }
}

