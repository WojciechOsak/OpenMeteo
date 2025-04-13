package com.wojciechosak.openmeteo.screen.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wojciechosak.openmeteo.R
import com.wojciechosak.openmeteo.domain.WeatherCode
import com.wojciechosak.openmeteo.utils.getCurrentUserLocation
import org.koin.compose.viewmodel.koinViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val uiState = viewModel.state.collectAsState().value
    val context = LocalContext.current

    getCurrentUserLocation(
        context,
        onGetCurrentLocationSuccess = {
            viewModel.loadGeoName(context, it)
        },
        onGetCurrentLocationFailed = {}
    )

    Box {
        Image(
            painter = painterResource(R.drawable.day_base),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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

@Composable
fun WeatherForecast(forecast: List<HomeScreenViewModel.DayForecast>?, modifier: Modifier = Modifier) {
    if (forecast == null) return
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            ConditionsLabel(it, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp))
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
private fun conditionText(weatherCode: WeatherCode): String {
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