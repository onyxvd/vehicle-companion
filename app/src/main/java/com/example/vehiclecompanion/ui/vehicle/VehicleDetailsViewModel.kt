package com.example.vehiclecompanion.ui.vehicle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehiclecompanion.data.db.model.Vehicle
import com.example.vehiclecompanion.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VehicleFormData(
    val id: Int? = null,
    val name: String? = "",
    val make: String = "",
    val model: String = "",
    val year: Int? = null,
    val vin: String? = "",
    val fuelType: String? = "",
    val isNewVehicle: Boolean = true
)

sealed interface VehicleDetailsScreenUiState {
    data object Loading : VehicleDetailsScreenUiState
    data class Success(
        val vehicleData: VehicleFormData,
        val formError: String? = null
    ) : VehicleDetailsScreenUiState

    data class Error(val message: String) : VehicleDetailsScreenUiState
}

@HiltViewModel
class VehicleDetailsViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val vehicleId: String? = savedStateHandle.get<String>("vehicleId")
    private val isNewVehicleEntry: Boolean = vehicleId == null || vehicleId == "new"

    private val _vehicleUiState = MutableStateFlow<VehicleDetailsScreenUiState>(
        if (isNewVehicleEntry) {
            VehicleDetailsScreenUiState.Success(VehicleFormData(isNewVehicle = true))
        } else {
            VehicleDetailsScreenUiState.Loading
        }
    )
    val vehicleUiState: StateFlow<VehicleDetailsScreenUiState> = _vehicleUiState.asStateFlow()

    private val _saveEventChannel = MutableSharedFlow<Unit>()
    val saveEventChannel: SharedFlow<Unit> = _saveEventChannel.asSharedFlow()

    init {
        if (!isNewVehicleEntry && vehicleId != null) {
            loadVehicle(vehicleId.toInt())
        }
    }

    private fun loadVehicle(id: Int) {
        viewModelScope.launch {
            val vehicle = vehicleRepository.getVehicle(id).firstOrNull()
            if (vehicle != null) {
                _vehicleUiState.value = VehicleDetailsScreenUiState.Success(
                    VehicleFormData(
                        id = vehicle.id,
                        name = vehicle.name,
                        make = vehicle.make,
                        model = vehicle.model,
                        year = vehicle.year,
                        vin = vehicle.vin,
                        fuelType = vehicle.fuelType,
                        isNewVehicle = false
                    )
                )
            } else {
                _vehicleUiState.value = VehicleDetailsScreenUiState.Error("Vehicle not found")
            }
        }
    }

    private fun updateStateWithNewFormData(newFormData: VehicleFormData) {
        if (_vehicleUiState.value is VehicleDetailsScreenUiState.Success) {
            _vehicleUiState.update {
                (it as VehicleDetailsScreenUiState.Success).copy(
                    vehicleData = newFormData,
                    formError = null
                )
            }
        }
    }

    fun updateName(name: String?) {
        (_vehicleUiState.value as? VehicleDetailsScreenUiState.Success)?.vehicleData?.let {
            updateStateWithNewFormData(it.copy(name = name))
        }
    }

    fun updateMake(make: String) {
        (_vehicleUiState.value as? VehicleDetailsScreenUiState.Success)?.vehicleData?.let {
            updateStateWithNewFormData(it.copy(make = make))
        }
    }

    fun updateModel(model: String) {
        (_vehicleUiState.value as? VehicleDetailsScreenUiState.Success)?.vehicleData?.let {
            updateStateWithNewFormData(it.copy(model = model))
        }
    }

    fun updateYear(year: Int?) {
        (_vehicleUiState.value as? VehicleDetailsScreenUiState.Success)?.vehicleData?.let {
            updateStateWithNewFormData(it.copy(year = year))
        }
    }

    fun updateVin(vin: String?) {
        (_vehicleUiState.value as? VehicleDetailsScreenUiState.Success)?.vehicleData?.let {
            updateStateWithNewFormData(it.copy(vin = vin))
        }
    }

    fun updateFuelType(fuelType: String?) {
        (_vehicleUiState.value as? VehicleDetailsScreenUiState.Success)?.vehicleData?.let {
            updateStateWithNewFormData(it.copy(fuelType = fuelType))
        }
    }

    fun saveVehicle() {
        val currentSuccessState =
            _vehicleUiState.value as? VehicleDetailsScreenUiState.Success ?: return

        val currentVehicleData = currentSuccessState.vehicleData
        if (currentVehicleData.make.isBlank() || currentVehicleData.model.isBlank()) {
            _vehicleUiState.value =
                currentSuccessState.copy(formError = "Make and Model cannot be empty")
            return
        }

        val vehicleToSave = Vehicle(
            id = if (currentVehicleData.isNewVehicle) null else currentVehicleData.id,
            name = currentVehicleData.name,
            make = currentVehicleData.make,
            model = currentVehicleData.model,
            year = currentVehicleData.year,
            vin = currentVehicleData.vin,
            fuelType = currentVehicleData.fuelType
        )

        viewModelScope.launch {
            if (currentVehicleData.isNewVehicle && vehicleToSave.id == null) {
                vehicleRepository.insertVehicle(vehicleToSave)
            } else {
                vehicleRepository.updateVehicle(vehicleToSave)
            }

            _saveEventChannel.emit(Unit)
        }
    }
}
