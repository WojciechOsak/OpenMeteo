package com.wojciechosak.openmeteo.data

import com.wojciechosak.openmeteo.R
import com.wojciechosak.openmeteo.domain.WeatherCode

fun weatherCodeToIcon(code: WeatherCode): Int {
    return when (code) {
        WeatherCode.CLEAR_SKY -> R.drawable.sun
        WeatherCode.MAINLY_CLEAR_PARTLY_CLOUDY_OVERCAST -> R.drawable.cloudy
        WeatherCode.FOG -> R.drawable.fog
        WeatherCode.SNOWFALL,
        WeatherCode.SNOW_SHOWERS,
        WeatherCode.SNOW_GRAINS -> R.drawable.snow

        WeatherCode.DRIZZLE,
        WeatherCode.FREEZING_DRIZZLE,
        WeatherCode.RAIN,
        WeatherCode.FREEZING_RAIN,
        WeatherCode.RAIN_SHOWERS -> R.drawable.rain

        WeatherCode.THUNDERSTORM -> R.drawable.storm
        WeatherCode.THUNDERSTORM_WITH_HAIL -> R.drawable.storm
    }
}
