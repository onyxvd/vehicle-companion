package com.example.vehiclecompanion.navigation

import com.example.vehiclecompanion.R

sealed interface Destination {
    var route: String
}

sealed interface TopLevelDestination : Destination {
    var icon: Int
    var name: String
}

data object Garage : TopLevelDestination {
    override var route = "garage"
    override var icon = R.drawable.ic_car
    override var name = "Garage"
}

data object Places : TopLevelDestination {
    override var route = "places"
    override var icon = R.drawable.ic_place
    override var name = "Places"
}
