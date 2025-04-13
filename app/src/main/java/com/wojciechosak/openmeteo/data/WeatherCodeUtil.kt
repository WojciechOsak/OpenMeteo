package com.wojciechosak.openmeteo.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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

@Composable
fun conditionText(weatherCode: WeatherCode): String {
    return stringResource(
        when (weatherCode) {
            WeatherCode.CLEAR_SKY -> R.string.clear_sky
            WeatherCode.MAINLY_CLEAR_PARTLY_CLOUDY_OVERCAST -> R.string.mainly_clear_partly_cloudy
            WeatherCode.FOG -> R.string.fog
            WeatherCode.DRIZZLE -> R.string.drizzle
            WeatherCode.FREEZING_DRIZZLE -> R.string.freezing_drizzle
            WeatherCode.RAIN -> R.string.rain
            WeatherCode.FREEZING_RAIN -> R.string.freezing_rain
            WeatherCode.SNOWFALL -> R.string.snowfall
            WeatherCode.SNOW_GRAINS -> R.string.snow_grains
            WeatherCode.RAIN_SHOWERS -> R.string.rain_showers
            WeatherCode.SNOW_SHOWERS -> R.string.snow_showers
            WeatherCode.THUNDERSTORM -> R.string.thunderstorm
            WeatherCode.THUNDERSTORM_WITH_HAIL -> R.string.thunderstorm_with_hail
        }
    )
}