package com.wojciechosak.openmeteo.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double,
    @SerializedName("generationtime_ms") var generationtimeMs: Double,
    @SerializedName("utc_offset_seconds") var utcOffsetSeconds: Int,
    @SerializedName("timezone") var timezone: String,
    @SerializedName("timezone_abbreviation") var timezoneAbbreviation: String,
    @SerializedName("elevation") var elevation: Int,
    @SerializedName("current_units") var currentUnits: CurrentUnits,
    @SerializedName("current") var current: Current,
    @SerializedName("daily_units") var dailyUnits: DailyUnits,
    @SerializedName("daily") var daily: Daily? = Daily()
)

data class CurrentUnits(
    @SerializedName("time") var time: String,
    @SerializedName("interval") var interval: String,
    @SerializedName("weather_code") var weatherCode: String,
    @SerializedName("temperature_2m") var temperature2m: String,
)

data class Current(
    @SerializedName("time") var time: String,
    @SerializedName("interval") var interval: Int,
    @SerializedName("weather_code") var weatherCode: Int,
    @SerializedName("temperature_2m") var temperature2m: Double,
    @SerializedName("wind_speed_10m")  val wind_speed_10m: Double,
    @SerializedName("relative_humidity_2m")  val relative_humidity_2m: Int,
)

data class DailyUnits(
    @SerializedName("time") var time: String,
    @SerializedName("temperature_2m_min") var temperature2mMin: String,
    @SerializedName("temperature_2m_max") var temperature2mMax: String,
    @SerializedName("weather_code") var weatherCode: String
)


data class Daily(
    @SerializedName("time") var time: ArrayList<String> = arrayListOf(),
    @SerializedName("temperature_2m_min") var temperature2mMin: ArrayList<Double> = arrayListOf(),
    @SerializedName("temperature_2m_max") var temperature2mMax: ArrayList<Double> = arrayListOf(),
    @SerializedName("weather_code") var weatherCode: ArrayList<Int> = arrayListOf()
)