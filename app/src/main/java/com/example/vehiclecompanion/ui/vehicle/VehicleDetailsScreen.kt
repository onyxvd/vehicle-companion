package com.example.vehiclecompanion.ui.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun VehicleDetailsScreen(
    viewModel: VehicleDetailsViewModel = hiltViewModel(),
    onSaveCompleted: () -> Unit = {}
) {
    val uiState by viewModel.vehicleUiState.collectAsState()

    LaunchedEffect(viewModel.saveEventChannel, onSaveCompleted) {
        viewModel.saveEventChannel.collectLatest {
            onSaveCompleted()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val currentUiState = uiState) {
            is VehicleDetailsScreenUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is VehicleDetailsScreenUiState.Error -> {
                Text(
                    text = "Error: ${currentUiState.message}",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }

            is VehicleDetailsScreenUiState.Success -> {
                VehicleFormContent(
                    formData = currentUiState.vehicleData,
                    formError = currentUiState.formError,
                    onNameChange = { viewModel.updateName(it) },
                    onMakeChange = { viewModel.updateMake(it) },
                    onModelChange = { viewModel.updateModel(it) },
                    onYearChange = { viewModel.updateYear(it) },
                    onVinChange = { viewModel.updateVin(it) },
                    onFuelTypeChange = { viewModel.updateFuelType(it) },
                    onSaveClicked = { viewModel.saveVehicle() }
                )
            }
        }
    }
}

@Composable
fun VehicleFormContent(
    formData: VehicleFormData,
    formError: String?,
    onNameChange: (String?) -> Unit,
    onMakeChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
    onYearChange: (Int?) -> Unit,
    onVinChange: (String?) -> Unit,
    onFuelTypeChange: (String?) -> Unit,
    onSaveClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = formData.name ?: "",
            onValueChange = onNameChange,
            label = { Text("Name (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null
        )

        OutlinedTextField(
            value = formData.make,
            onValueChange = onMakeChange,
            label = { Text("Make") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null && formData.make.isBlank()
        )

        OutlinedTextField(
            value = formData.model,
            onValueChange = onModelChange,
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null && formData.model.isBlank()
        )

        OutlinedTextField(
            value = formData.year?.toString() ?: "",
            onValueChange = { yearString -> onYearChange(yearString.toIntOrNull()) },
            label = { Text("Year (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null
        )

        OutlinedTextField(
            value = formData.vin ?: "",
            onValueChange = onVinChange,
            label = { Text("VIN (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null
        )

        OutlinedTextField(
            value = formData.fuelType ?: "",
            onValueChange = onFuelTypeChange,
            label = { Text("Fuel Type (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null
        )

        formError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSaveClicked,
            modifier = Modifier.fillMaxWidth(),
            enabled = formData.make.isNotBlank() && formData.model.isNotBlank()
        ) {
            Text("Save Vehicle")
        }
    }
}
