package com.wojciechosak.openmeteo.screen.details

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojciechosak.openmeteo.data.weatherCodeToIcon
import com.wojciechosak.openmeteo.domain.IWeatherRepository
import com.wojciechosak.openmeteo.domain.WeatherCode
import com.wojciechosak.openmeteo.network.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    private val weatherRepository: IWeatherRepository
) : ViewModel() {

    data class ViewState(
        val locationName: String? = null,
        val temperature: Double? = null,
        val conditions: WeatherCode? = null,
        @DrawableRes val weatherIcon: Int? = null,
        val windSpeedKmPerHour: Double? = null,
        val humidityPercentage: Double? = null
    )

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState?> = _state

    fun loadData(city: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val result = weatherRepository.loadCityDetails(lat, lon)
            when (result) {
                is ResultWrapper.GenericError -> {
                    // TODO handle errors
                }

                ResultWrapper.NetworkError -> {
                    // TODO handle errors
                }

                is ResultWrapper.Success -> {
                    _state.value = _state.value.copy(
                        locationName = city,
                        temperature = result.value.currentTemperatureCelsius,
                        humidityPercentage = result.value.humidity!!,
                        windSpeedKmPerHour = result.value.windSpeedKmPerHour!!,
                        conditions = result.value.currentWeatherCode,
                        weatherIcon = weatherCodeToIcon(result.value.currentWeatherCode),
                    ).also {
                        println("@@@ XXXX $it")
                    }
                }
            }
        }
    }

}