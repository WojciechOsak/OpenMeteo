package com.wojciechosak.openmeteo.di

import com.wojciechosak.openmeteo.data.WeatherRepository
import com.wojciechosak.openmeteo.data.WeatherResponseMapper
import com.wojciechosak.openmeteo.domain.IWeatherRepository
import org.koin.dsl.module

val dataModule = module {
    factory { WeatherResponseMapper() }
    factory<IWeatherRepository> {
        WeatherRepository(
            dispatcherProvider = get(),
            weatherService = get(),
            responseMapper = get()
        )
    }
}