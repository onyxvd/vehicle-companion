package com.example.vehiclecompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vehiclecompanion.ui.place.PlaceDetailsScreen
import com.example.vehiclecompanion.ui.place.PlaceDetailsViewModel
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

            PlacesScreen(
                viewModel,
                onPlaceClicked = { place ->
                    navController.navigate(PlaceDetails.createNavigationRoute(place))
                }
            )
        }
        composable(
            route = PlaceDetails.route,
            arguments = listOf(navArgument(PlaceDetails.ARG_PLACE_ID) {
                type = NavType.IntType
            }, navArgument(PlaceDetails.ARG_PLACE_NAME) {
                type = NavType.StringType
            })
        ) {
            val viewModel: PlaceDetailsViewModel = hiltViewModel()
            PlaceDetailsScreen(
                viewModel = viewModel,
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}
