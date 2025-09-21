package com.example.vehiclecompanion.ui.place

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PlacesScreen(
    viewModel: PlacesViewModel
) {
    val uiState = viewModel.uiState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is PlacesUiState.Success -> {
                Text(
                    text = uiState.places.toString(),
                    modifier = Modifier,
                )
            }

            else -> {
                Text(text = "Loading...")
            }
        }

    }
}
