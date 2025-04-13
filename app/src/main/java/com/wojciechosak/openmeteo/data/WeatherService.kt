package com.wojciechosak.openmeteo.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "temperature_2m_min,temperature_2m_max,weather_code",
        @Query("current") current: String = "weather_code,temperature_2m"
    ): WeatherResponse
}