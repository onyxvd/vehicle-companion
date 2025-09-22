package com.example.vehiclecompanion.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vehiclecompanion.R
import com.example.vehiclecompanion.navigation.AppNavHost
import com.example.vehiclecompanion.navigation.Garage
import com.example.vehiclecompanion.navigation.PlaceDetails
import com.example.vehiclecompanion.navigation.Places
import com.example.vehiclecompanion.ui.theme.VehicleCompanionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleCompanionApp() {
    VehicleCompanionTheme {
        val navController = rememberNavController()
        val topLevelDestinations = listOf(
            Garage,
            Places
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val context = LocalContext.current
        val isTopLevelDestination = topLevelDestinations.any {
            it.route == currentDestination?.route
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        val title = getScreenTitleForDestination(context, navBackStackEntry)
                        Text(title)
                    },
                    navigationIcon = {
                        if (isTopLevelDestination) return@TopAppBar
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.navigate_up)
                            )
                        }
                    }
                )
            },
            bottomBar = {
                if (!isTopLevelDestination) return@Scaffold
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        val selected = currentDestination?.route == destination.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    ImageVector.vectorResource(destination.icon),
                                    contentDescription = destination.name
                                )
                            },
                            label = { Text(destination.name) },
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }

                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

private fun getScreenTitleForDestination(
    context: Context,
    backStackEntry: NavBackStackEntry?
): String {
    return when (backStackEntry?.destination?.route) {
        Garage.route -> context.getString(R.string.garage_screen_title)
        Places.route -> context.getString(R.string.places_screen_title)
        else -> {
            if (backStackEntry?.arguments?.getString(PlaceDetails.ARG_SCREEN_TITLE) != null) {
                backStackEntry.arguments?.getString(PlaceDetails.ARG_SCREEN_TITLE)!!
            } else {
                context.getString(R.string.app_name)
            }
        }
    }
}
