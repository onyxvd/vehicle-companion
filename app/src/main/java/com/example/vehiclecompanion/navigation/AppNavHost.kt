package com.example.vehiclecompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vehiclecompanion.ui.place.PlacesScreen
import com.example.vehiclecompanion.ui.place.PlacesViewModel
import com.example.vehiclecompanion.ui.vehicle.GarageScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Garage.route,
        modifier = modifier
    ) {
        composable(route = Garage.route) {
            GarageScreen()
        }
        composable(route = Places.route) {
            val viewModel: PlacesViewModel = hiltViewModel()

            PlacesScreen(viewModel)
        }
    }
}
