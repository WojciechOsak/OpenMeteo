package com.wojciechosak.openmeteo.screen.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.wojciechosak.openmeteo.R
import com.wojciechosak.openmeteo.screen.Screen
import com.wojciechosak.openmeteo.utils.RequestLocationPermissionUsingRememberMultiplePermissionsState

// TODO blinking X issue after granting permissions

@Composable
fun PermissionScreen(navController: NavController) {
    var locationPermissionsGranted by remember { mutableStateOf(false) }
    var tryCounter by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    RequestLocationPermissionUsingRememberMultiplePermissionsState(
        onPermissionGranted = {
            locationPermissionsGranted = true
        },
        onPermissionsRevoked = {
            locationPermissionsGranted = false
        },
        onPermissionDenied = {
            locationPermissionsGranted = false
        },
        tryCounter = tryCounter
    )
    if (locationPermissionsGranted) {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.PermissionScreen.route) { inclusive = true }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.baseline_close_24), null)
            val message = stringResource(
                if (tryCounter > 0) {
                    R.string.open_app_settings
                } else {
                    R.string.permissions_not_granted
                }
            )
            Text(message)
            Button(onClick = {
                if (tryCounter > 0) {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    tryCounter++
                }
            }) {
                Text(
                    stringResource(
                        if (tryCounter > 0) {
                            R.string.open
                        } else {
                            R.string.retry
                        }
                    )
                )
            }
        }
    }
}