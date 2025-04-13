package com.wojciechosak.openmeteo.screen.home

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel(

) : ViewModel() {

    data class ViewState(
        val currentLocationCity: String,
        val currentLocationCountry: String,
        val temperatureCelsius: Float,
        val conditions: String,
        @DrawableRes val conditionsIcon: Int,
        val forecast: List<DayForecast>,
        val search: String,
        val searchResult: List<String>
    )

    data class DayForecast(
        val date: String,
        val highTemperatureCelsius: Float,
        val lowTemperatureCelsius: Float,
        val conditions: String,
        @DrawableRes val conditionsIcon: Int
    )

    private val _state = MutableStateFlow<ViewState?>(null)
    val state: StateFlow<ViewState?> = _state
}