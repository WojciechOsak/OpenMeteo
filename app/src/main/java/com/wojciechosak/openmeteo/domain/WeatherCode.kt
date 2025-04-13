package com.wojciechosak.openmeteo.domain

enum class WeatherCode(val codes: List<Int>) {
    CLEAR_SKY(listOf(0)),
    MAINLY_CLEAR_PARTLY_CLOUDY_OVERCAST(listOf(1, 2, 3)),
    FOG(listOf(45, 48)),
    DRIZZLE(listOf(51, 53, 55)),
    FREEZING_DRIZZLE(listOf(56, 57)),
    RAIN(listOf(61, 63, 65)),
    FREEZING_RAIN(listOf(66, 67)),
    SNOWFALL(listOf(71, 73, 75)),
    SNOW_GRAINS(listOf(77)),
    RAIN_SHOWERS(listOf(80, 81, 82)),
    SNOW_SHOWERS(listOf(85, 86)),
    THUNDERSTORM(listOf(95)),
    THUNDERSTORM_WITH_HAIL(listOf(96, 99));

    companion object {
        private val codeMap = entries.flatMap { entry ->
            entry.codes.map { it to entry }
        }.toMap()

        fun fromCode(code: Int): WeatherCode? = codeMap[code]
    }
}