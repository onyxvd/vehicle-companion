package com.example.vehiclecompanion.ui.place

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehiclecompanion.R
import com.example.vehiclecompanion.data.network.Place
import com.example.vehiclecompanion.data.repository.PlaceRepository
import com.example.vehiclecompanion.navigation.PlaceDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PlaceDetailsUiState {
    data class Success(val place: Place) : PlaceDetailsUiState
    data object Loading : PlaceDetailsUiState
    data class Error(val message: String) : PlaceDetailsUiState
}

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
) : ViewModel() {

    var uiState: PlaceDetailsUiState by mutableStateOf(PlaceDetailsUiState.Loading)
        private set

    private val placeId: Int? =
        savedStateHandle[PlaceDetails.ARG_PLACE_ID]


    init {
        if (placeId != null) {
            loadPlaceDetails()
        } else {
            uiState =
                PlaceDetailsUiState.Error(context.getString(R.string.place_id_not_provided))
        }
    }

    fun loadPlaceDetails() {
        uiState = PlaceDetailsUiState.Loading
        viewModelScope.launch {
            uiState = try {
                val place = placeRepository.getPlaceDetails(placeId!!)
                PlaceDetailsUiState.Success(place)
            } catch (e: Exception) {
                PlaceDetailsUiState.Error(context.getString(R.string.place_id_not_provided))
            }
        }
    }
}
