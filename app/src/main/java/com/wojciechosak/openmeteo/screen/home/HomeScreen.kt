package com.wojciechosak.openmeteo.screen.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wojciechosak.openmeteo.R
import com.wojciechosak.openmeteo.data.conditionText
import com.wojciechosak.openmeteo.domain.WeatherCode
import com.wojciechosak.openmeteo.screen.Screen
import com.wojciechosak.openmeteo.utils.getCurrentUserLocation
import okhttp3.internal.wait
import org.koin.compose.viewmodel.koinViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val uiState = viewModel.state.collectAsState().value
    val context = LocalContext.current
    var textFieldValue by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        getCurrentUserLocation(
            context,
            onGetCurrentLocationSuccess = {
                viewModel.loadGeoName(context, it)
            },
            onGetCurrentLocationFailed = {}
        )
    }

    Box {
        Image(
            painter = painterResource(R.drawable.day_base),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    Column {
        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                viewModel.citySearch(it)
            },
            label = { Text(stringResource(R.string.enter_city)) },
            maxLines = 1,
            textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Normal),
            colors = TextFieldDefaults.colors().copy(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 12.dp,
                    end = 12.dp
                )
                .border(1.dp, Color.Black)
                .clearFocusOnKeyboardDismiss(),
        )
        if (uiState.loadingError) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.error_loading_data), color = Color.White)
                Button(onClick = {
                    uiState.locationCoords?.let {
                        viewModel.loadGeoName(context, latLon = it)
                    }
                }) {
                    Text(stringResource(R.string.retry))
                }
            }
        } else {
            LazyColumn {
                items(uiState.searchResult) {
                    Row(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.Detail.route.plus("/${it.name}/${it.lat}/${it.lon}"))
                            }) {
                        Text(it.name, modifier = Modifier.padding(16.dp), fontSize = 24.sp, fontWeight = FontWeight.Light)
                    }
                }
            }
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                if (uiState.dataLoaded) {
                    uiState.currentLocation?.let {
                        Text(
                            it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 40.sp
                        )
                    }
                    CurrentWeather(uiState, modifier = Modifier.padding(12.dp))
                    WeatherForecast(uiState.forecast, modifier = Modifier.padding(bottom = 16.dp))
                }
            }
        }

    }
}

@Composable
fun WeatherForecast(forecast: List<HomeScreenViewModel.DayForecast>?, modifier: Modifier = Modifier) {
    if (forecast == null) return
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(forecast) { ForecastItem(it) }
    }
}

@Composable
fun ForecastItem(forecast: HomeScreenViewModel.DayForecast) {
    Column(
        modifier = Modifier
            .border(1.dp, Color.White)
            .defaultMinSize(minWidth = 160.dp)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = forecast.date.format(DateTimeFormatter.ofPattern("E dd")),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W200,
            color = Color.White
        )
        Image(
            painter = painterResource(id = forecast.conditionsIcon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = conditionText(forecast.conditions),
            fontWeight = FontWeight.Thin,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = Color.White
        )
        Text(
            text = "min: %.2f°C\nmax: %.2f°C".format(forecast.minTemperatureCelsius, forecast.maxTemperatureCelsius),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CurrentWeather(uiState: HomeScreenViewModel.ViewState, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center) {
        uiState.temperatureCelsius?.let {
            Temperature(
                temperature = it,
                iconRes = uiState.conditionsIcon,
                modifier = modifier.fillMaxWidth()
            )
        }
        uiState.conditions?.let {
            ConditionsLabel(
                it, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun ConditionsLabel(weatherCode: WeatherCode, modifier: Modifier) {
    val conditionText = conditionText(weatherCode)
    Text(
        text = conditionText,
        textAlign = TextAlign.Center,
        modifier = modifier,
        color = Color.White,
    )
}

@Composable
private fun Temperature(temperature: Double, @DrawableRes iconRes: Int?, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        iconRes?.let {
            Image(painter = painterResource(iconRes), contentDescription = null, modifier = Modifier.size(42.dp))
        }
        Text(
            text = "%.1f°C".format(temperature),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 34.sp,
            textAlign = TextAlign.Center
        )
    }
}


// from: https://www.reddit.com/r/androiddev/comments/1fc1rgt/about_focusunfocus_on_textfield_with_jetpack/
@OptIn(ExperimentalLayoutApi::class)
@Stable
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current

        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }

    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) keyboardAppearedSinceLastFocused = false
        }
    }
}
