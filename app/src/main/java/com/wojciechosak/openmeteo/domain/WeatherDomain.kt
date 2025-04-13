package com.wojciechosak.openmeteo.domain

data class WeatherDomain(
    var currentTemperatureCelsius: Double,
    var dailyForecast: List<DayForecast>,
    val currentWeatherCode: WeatherCode
)

data class DayForecast(
    val time: String,
    val index: Int,
    val minTemperatureCelsius: Double,
    val maxTemperatureCelsius: Double,
    val weatherCode: WeatherCode
)
