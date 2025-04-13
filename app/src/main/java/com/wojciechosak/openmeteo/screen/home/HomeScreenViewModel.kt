package com.wojciechosak.openmeteo.screen.home

import android.content.Context
import android.location.Geocoder
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojciechosak.openmeteo.R
import com.wojciechosak.openmeteo.domain.IWeatherRepository
import com.wojciechosak.openmeteo.domain.WeatherCode
import com.wojciechosak.openmeteo.network.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class HomeScreenViewModel(
    private val weatherRepository: IWeatherRepository
) : ViewModel() {

    data class ViewState(
        val currentLocation: String? = null,
        val locationCoords: Pair<Double, Double>? = null,
        val temperatureCelsius: Double? = null,
        val conditions: WeatherCode? = null,
        @DrawableRes val conditionsIcon: Int? = null,
        val forecast: List<DayForecast>? = null,
        val search: String? = null,
        val searchResult: List<String>? = null,
        val dataLoaded: Boolean = false
    )

    data class DayForecast(
        val date: String,
        val maxTemperatureCelsius: Double,
        val minTemperatureCelsius: Double,
        val conditions: WeatherCode,
        @DrawableRes val conditionsIcon: Int
    )

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state

    fun loadGeoName(context: Context, latLon: Pair<Double, Double>) {
        val (lat, lon) = latLon
        val geocoder = Geocoder(context, Locale.getDefault())
        // TODO deprecated, improve by using different methods for different api versions
        geocoder.getFromLocation(lat, lon, 1)?.firstOrNull()?.let { location ->
            val city = location.locality ?: location.subAdminArea
            val country = location.countryName
            modifyState { oldState -> oldState.copy(currentLocation = "$country, $city") }
        }
        if (state.value.locationCoords == null) {
            loadWeatherData(latLon)
            modifyState { oldState -> oldState.copy(locationCoords = latLon) }
        }
    }

    private fun loadWeatherData(latLon: Pair<Double, Double>) {
        val (lat, lon) = latLon
        viewModelScope.launch {
            val response = weatherRepository.loadWeatherData(lat, lon)
            when (response) {
                is ResultWrapper.GenericError -> {
                    // TODO handle generic error on UI
                    modifyState { oldState -> oldState.copy(dataLoaded = false) }
                }

                ResultWrapper.NetworkError -> {
                    // TODO handle network error on UI
                    modifyState { oldState -> oldState.copy(dataLoaded = false) }
                }

                is ResultWrapper.Success -> {
                    val responseValue = response.value
                    modifyState { oldState ->
                        oldState.copy(
                            temperatureCelsius = responseValue.currentTemperatureCelsius,
                            forecast = responseValue.dailyForecast.map {
                                DayForecast(
                                    conditions = it.weatherCode,
                                    date = it.time,
                                    maxTemperatureCelsius = it.maxTemperatureCelsius,
                                    minTemperatureCelsius = it.minTemperatureCelsius,
                                    conditionsIcon = weatherCodeToIcon(it.weatherCode)
                                )
                            },
                            conditionsIcon = weatherCodeToIcon(responseValue.currentWeatherCode),
                            conditions = responseValue.currentWeatherCode,
                            dataLoaded = true
                        )
                    }
                }
            }
        }
    }

    private fun weatherCodeToIcon(code: WeatherCode): Int {
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

    private fun modifyState(modification: (ViewState) -> ViewState) {
        _state.value = modification(state.value)
    }
}