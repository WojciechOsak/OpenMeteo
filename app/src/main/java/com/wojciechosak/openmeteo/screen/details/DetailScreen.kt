package com.wojciechosak.openmeteo.screen.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wojciechosak.openmeteo.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailScreen(location: String, lat: Float, lon: Float) {
    val viewModel = koinViewModel<DetailScreenViewModel>()
    LaunchedEffect(Unit) {
        viewModel.loadData(location, lat.toDouble(), lon.toDouble())
    }
    val uiState = viewModel.state.collectAsState().value
    Box {
        Image(
            painter = painterResource(R.drawable.day_base),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                start = 12.dp,
                end = 12.dp
            )
    ) {
        uiState?.locationName?.let {
            Text(it, fontSize = 52.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        uiState?.humidityPercentage?.let {
            Text(
                "Humidity: %.2f".format(it),
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            uiState?.weatherIcon?.let {
                Icon(painter = painterResource(it), contentDescription = null, modifier = Modifier.size(60.dp))
            }
        }
    }
}