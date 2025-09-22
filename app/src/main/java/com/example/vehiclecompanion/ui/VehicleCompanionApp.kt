package com.example.vehiclecompanion.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vehiclecompanion.navigation.AppNavHost
import com.example.vehiclecompanion.navigation.Garage
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

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
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
            },
            topBar = {
                TopAppBar(
                    title = {
                        val title = topLevelDestinations.find {
                            it.route == currentDestination?.route
                        }?.name ?: "Vehicle Companion"
                        Text(title)
                    }
                )
            }
        ) { innerPadding ->
            AppNavHost(
                navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
