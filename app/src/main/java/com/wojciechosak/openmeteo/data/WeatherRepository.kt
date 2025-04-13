package com.wojciechosak.openmeteo.data

import com.wojciechosak.openmeteo.core.DispatcherProvider
import com.wojciechosak.openmeteo.domain.IWeatherRepository
import com.wojciechosak.openmeteo.domain.WeatherDomain
import com.wojciechosak.openmeteo.network.ResultWrapper
import com.wojciechosak.openmeteo.network.safeApiCall

class WeatherRepository(
    private val weatherService: WeatherService,
    private val responseMapper: WeatherResponseMapper,
    private val dispatcherProvider: DispatcherProvider
) : IWeatherRepository {

    override suspend fun loadWeatherData(lat: Double, lon: Double): ResultWrapper<WeatherDomain> {
        val response = safeApiCall(dispatcher = dispatcherProvider.io) {
            weatherService.getWeatherForecast(lat, lon)
        }

        return when (response) {
            is ResultWrapper.GenericError -> response
            is ResultWrapper.NetworkError -> response
            is ResultWrapper.Success -> {
                ResultWrapper.Success(responseMapper.map(response.value))
            }
        }
    }

    override suspend fun loadCityDetails(lat: Double, lon: Double): ResultWrapper<WeatherDomain> {
        val response = safeApiCall(dispatcher = dispatcherProvider.io) {
            weatherService.getCityWeatherWithoutForecast(lat, lon)
        }

        return when (response) {
            is ResultWrapper.GenericError -> response
            is ResultWrapper.NetworkError -> response
            is ResultWrapper.Success -> {
                ResultWrapper.Success(responseMapper.map(response.value))
            }
        }
    }
}