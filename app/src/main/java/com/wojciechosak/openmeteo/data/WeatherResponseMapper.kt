package com.wojciechosak.openmeteo.data

import com.wojciechosak.openmeteo.domain.DayForecast
import com.wojciechosak.openmeteo.domain.WeatherCode
import com.wojciechosak.openmeteo.domain.WeatherDomain

class WeatherResponseMapper {
    fun map(from: WeatherResponse): WeatherDomain {
        val dailyForecast = from.daily?.run {
            val objectsCount = time.size
            val result = mutableListOf<DayForecast>()
            for (i in 0..<objectsCount) {
                result.add(
                    DayForecast(
                        time = time[i],
                        index = i,
                        minTemperatureCelsius = temperature2mMin[i],
                        maxTemperatureCelsius = temperature2mMax[i],
                        weatherCode = WeatherCode.fromCode(weatherCode[i])!!,
                    )
                )
            }
            result
        }
        return WeatherDomain(
            currentTemperatureCelsius = from.current.temperature2m,
            dailyForecast = dailyForecast,
            currentWeatherCode = WeatherCode.fromCode(from.current.weatherCode)!!,
            humidity = from.current.relative_humidity_2m.toDouble(),
            windSpeedKmPerHour = from.current.wind_speed_10m
        )
    }
}