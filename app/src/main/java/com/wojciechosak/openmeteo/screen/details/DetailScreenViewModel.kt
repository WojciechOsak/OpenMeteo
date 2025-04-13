package com.wojciechosak.openmeteo.screen.details

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailScreenViewModel : ViewModel() {

    data class ViewState(
        val locationName: String,
        val temperature: Float,
        val conditions: String,
        @DrawableRes val weatherIcon: Int,
        val windSpeedKmPerHour: Float,
        val humidityPercentage: Float
    )

    private val _state = MutableStateFlow<ViewState?>(null)
    val state: StateFlow<ViewState?> = _state
}