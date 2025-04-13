package com.wojciechosak.openmeteo.domain

import com.wojciechosak.openmeteo.network.ResultWrapper

interface IWeatherRepository {
    suspend fun loadWeatherData(lat: Double, lon: Double): ResultWrapper<WeatherDomain>

    suspend fun loadCityDetails(lat: Double, lon: Double): ResultWrapper<WeatherDomain>
}