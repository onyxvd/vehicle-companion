package com.example.vehiclecompanion.ui.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vehiclecompanion.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun VehicleDetailsScreen(
    viewModel: VehicleDetailsViewModel = hiltViewModel(),
    onSaveCompleted: () -> Unit = {},
    onDeleteCompleted: () -> Unit = {}
) {
    val uiState by viewModel.vehicleUiState.collectAsState()

    LaunchedEffect(viewModel.saveEventChannel, onSaveCompleted) {
        viewModel.saveEventChannel.collectLatest {
            onSaveCompleted()
        }
    }

    LaunchedEffect(viewModel.deleteEventChannel, onDeleteCompleted) {
        viewModel.deleteEventChannel.collectLatest {
            onDeleteCompleted()
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
                    isNewVehicle = currentUiState.vehicleData.isNewVehicle,
                    onNameChange = { viewModel.updateName(it) },
                    onMakeChange = { viewModel.updateMake(it) },
                    onModelChange = { viewModel.updateModel(it) },
                    onYearChange = { viewModel.updateYear(it) },
                    onVinChange = { viewModel.updateVin(it) },
                    onFuelTypeChange = { viewModel.updateFuelType(it) },
                    onSaveClicked = { viewModel.saveVehicle() },
                    onDeleteClicked = { viewModel.deleteVehicle() }
                )
            }
        }
    }
}

@Composable
fun VehicleFormContent(
    formData: VehicleFormData,
    formError: String?,
    isNewVehicle: Boolean,
    onNameChange: (String?) -> Unit,
    onMakeChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
    onYearChange: (Int?) -> Unit,
    onVinChange: (String?) -> Unit,
    onFuelTypeChange: (String?) -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: () -> Unit
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
            label = { Text(stringResource(R.string.vehicle_details_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null
        )

        OutlinedTextField(
            value = formData.make,
            onValueChange = onMakeChange,
            label = { Text(stringResource(R.string.vehicle_details_make_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null && formData.make.isBlank()
        )

        OutlinedTextField(
            value = formData.model,
            onValueChange = onModelChange,
            label = { Text(stringResource(R.string.vehicle_details_model_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null && formData.model.isBlank()
        )

        OutlinedTextField(
            value = formData.year?.toString() ?: "",
            onValueChange = { yearString -> onYearChange(yearString.toIntOrNull()) },
            label = { Text(stringResource(R.string.vehicle_details_year_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null
        )

        OutlinedTextField(
            value = formData.vin ?: "",
            onValueChange = onVinChange,
            label = { Text(stringResource(R.string.vehicle_details_vin_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = formError != null
        )

        OutlinedTextField(
            value = formData.fuelType ?: "",
            onValueChange = onFuelTypeChange,
            label = { Text(stringResource(R.string.vehicle_details_fuel_type_label)) },
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isNewVehicle) {
                OutlinedButton(
                    onClick = onDeleteClicked,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            }
            Button(
                onClick = onSaveClicked,
                modifier = Modifier.weight(1f),
                enabled = formData.make.isNotBlank() && formData.model.isNotBlank()
            ) {
                Text(if (isNewVehicle) stringResource(R.string.save_vehicle) else stringResource(R.string.update_vehicle))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
