package com.example.vehiclecompanion.ui.vehicle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vehiclecompanion.data.db.model.Vehicle

@Composable
fun GarageScreen(
    viewModel: GarageViewModel,
    onAddVehicleClicked: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is GarageUiState.Loading -> {
            LoadingScreen()
        }

        is GarageUiState.Success -> {
            GarageListScreen(
                vehicles = (uiState as GarageUiState.Success).vehicles,
                onAddVehicleClicked = onAddVehicleClicked
            )
        }

    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Loading vehicles...")
    }
}

@Composable
fun GarageListScreen(
    vehicles: List<Vehicle>,
    onAddVehicleClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (vehicles.isEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Your garage is empty.")
                Button(onClick = onAddVehicleClicked) {
                    Text(text = "Add Vehicle")
                }
            }

        } else {
            Text(text = "You have ${vehicles.size} vehicles in your garage.")
        }

    }
}
