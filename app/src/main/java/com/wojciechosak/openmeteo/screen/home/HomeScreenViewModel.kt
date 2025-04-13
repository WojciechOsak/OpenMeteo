package com.wojciechosak.openmeteo.screen.home

import android.content.Context
import android.location.Geocoder
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojciechosak.openmeteo.data.weatherCodeToIcon
import com.wojciechosak.openmeteo.domain.ILocationRepository
import com.wojciechosak.openmeteo.domain.IWeatherRepository
import com.wojciechosak.openmeteo.domain.LocationDomain
import com.wojciechosak.openmeteo.domain.WeatherCode
import com.wojciechosak.openmeteo.network.ResultWrapper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    private val weatherRepository: IWeatherRepository,
    private val locationRepository: ILocationRepository
) : ViewModel() {

    data class ViewState(
        val currentLocation: String? = null,
        val locationCoords: Pair<Double, Double>? = null,
        val temperatureCelsius: Double? = null,
        val conditions: WeatherCode? = null,
        @DrawableRes val conditionsIcon: Int? = null,
        val forecast: List<DayForecast>? = null,
        val searchResult: List<LocationDomain> = emptyList(),
        val dataLoaded: Boolean = false,
        val loadingError: Boolean = false
    )

    data class DayForecast(
        val date: String,
        val maxTemperatureCelsius: Double,
        val minTemperatureCelsius: Double,
        val conditions: WeatherCode,
        @DrawableRes val conditionsIcon: Int
    )

    private val _state = MutableStateFlow(ViewState())
    private val searchInput = MutableStateFlow("")

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
                    modifyState { oldState -> oldState.copy(dataLoaded = false, loadingError = true) }
                }

                ResultWrapper.NetworkError -> {
                    modifyState { oldState -> oldState.copy(dataLoaded = false, loadingError = true) }
                }

                is ResultWrapper.Success -> {
                    val responseValue = response.value
                    modifyState { oldState ->
                        oldState.copy(
                            temperatureCelsius = responseValue.currentTemperatureCelsius,
                            forecast = responseValue.dailyForecast?.drop(1)?.map {
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

    init {
        searchInput
            .filter { it.isNotEmpty() }
            .debounce(500L)
            .onEach { city ->
                val locations = locationRepository.searchLocation(city)
                when (locations) {
                    is ResultWrapper.GenericError -> TODO()
                    ResultWrapper.NetworkError -> TODO()
                    is ResultWrapper.Success -> {
                        modifyState { oldState -> oldState.copy(searchResult = locations.value) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun citySearch(city: String) {
        searchInput.value = city
    }

    private fun modifyState(modification: (ViewState) -> ViewState) {
        _state.value = modification(state.value)
    }
}