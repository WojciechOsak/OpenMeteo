package com.wojciechosak.openmeteo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.wojciechosak.openmeteo.navigation.NavigationStack
import com.wojciechosak.openmeteo.ui.theme.OpenMeteoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenMeteoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavigationStack()
                }
            }
        }
    }
}
