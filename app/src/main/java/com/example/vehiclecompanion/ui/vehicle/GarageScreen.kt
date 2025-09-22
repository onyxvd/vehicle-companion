package com.example.vehiclecompanion.ui.vehicle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vehiclecompanion.R
import com.example.vehiclecompanion.data.db.model.Vehicle

@Composable
fun GarageScreen(
    viewModel: GarageViewModel,
    onAddVehicleClicked: () -> Unit = {},
    onVehicleClicked: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is GarageUiState.Loading -> {
            LoadingScreen()
        }

        is GarageUiState.Success -> {
            GarageListScreen(
                vehicles = (uiState as GarageUiState.Success).vehicles,
                onAddVehicleClicked = onAddVehicleClicked,
                onVehicleClicked = onVehicleClicked
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
    onAddVehicleClicked: () -> Unit = {},
    onVehicleClicked: (Int) -> Unit = {}
) {
    if (vehicles.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Your garage is empty.")
                Button(onClick = onAddVehicleClicked) {
                    Text(text = "Add Vehicle")
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vehicles) { vehicle ->
                VehicleCard(
                    vehicle = vehicle,
                    onVehicleClicked = onVehicleClicked
                )
            }
        }
    }
}

@Composable
fun VehicleCard(
    vehicle: Vehicle,
    onVehicleClicked: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onVehicleClicked(vehicle.id!!) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_car),
                    contentDescription = "Vehicle Icon",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = vehicle.name ?: "${vehicle.year} ${vehicle.make} ${vehicle.model}",
                    style = MaterialTheme.typography.titleLarge
                )
                if (vehicle.name != null) {
                    Text(
                        text = "${vehicle.year} ${vehicle.make} ${vehicle.model}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
    }
}
