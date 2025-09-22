package com.example.vehiclecompanion.ui.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehiclecompanion.data.db.model.Vehicle
import com.example.vehiclecompanion.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface GarageUiState {
    data class Success(val vehicles: List<Vehicle>) : GarageUiState
    object Loading : GarageUiState
}

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    val uiState: StateFlow<GarageUiState> = vehicleRepository.getAllVehicles().map { vehicles ->
        GarageUiState.Success(vehicles)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GarageUiState.Loading
    )

}
