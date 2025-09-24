package com.example.vehiclecompanion.ui.place

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehiclecompanion.data.network.DiscoverPlace
import com.example.vehiclecompanion.data.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PlacesUiState {
    data class Success(val places: List<DiscoverPlace>) : PlacesUiState
    data object Loading : PlacesUiState
    data class Error(val message: String) : PlacesUiState
}

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    var uiState: PlacesUiState by mutableStateOf(PlacesUiState.Loading)
        private set

    init {
        discoverPlaces()
    }

    fun discoverPlaces() {
        viewModelScope.launch {
            uiState = PlacesUiState.Loading
            uiState = try {
                val places = placeRepository.discoverPlaces()
                PlacesUiState.Success(places.places)
            } catch (e: Exception) {
                PlacesUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

}
