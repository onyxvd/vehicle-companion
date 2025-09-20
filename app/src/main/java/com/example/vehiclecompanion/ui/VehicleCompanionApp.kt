package com.example.vehiclecompanion.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vehiclecompanion.ui.theme.VehicleCompanionTheme

@Composable
fun VehicleCompanionApp() {
    VehicleCompanionTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Vehicle Companion",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
