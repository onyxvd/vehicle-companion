package com.example.vehiclecompanion.navigation

import com.example.vehiclecompanion.R
import com.example.vehiclecompanion.data.network.DiscoverPlace

sealed interface Destination {
    val route: String

    val ARG_SCREEN_TITLE: String
        get() = "screenTitle"
}

sealed interface TopLevelDestination : Destination {
    val icon: Int
    val name: String

}

data object Garage : TopLevelDestination {
    override val route = "garage"
    override val icon = R.drawable.ic_car
    override val name = "Garage"
}

data object Places : TopLevelDestination {
    override val route = "places"
    override val icon = R.drawable.ic_place
    override val name = "Places"
}

data object PlaceDetails : Destination {
    const val ROUTE_BASE = "place_details"
    const val ARG_PLACE_ID = "placeId"
    override val route = "$ROUTE_BASE/{$ARG_PLACE_ID}/{${ARG_SCREEN_TITLE}}"

    fun createNavigationRoute(place: DiscoverPlace): String {
        return "$ROUTE_BASE/${place.id}/${place.name}"
    }
}

data object VehicleDetails : Destination {
    const val ROUTE_BASE = "vehicle_details"
    const val ARG_VEHICLE_ID = "vehicleId"

    override val route = "$ROUTE_BASE/{$ARG_VEHICLE_ID}/{${ARG_SCREEN_TITLE}}"

    fun createNavigationRoute(id: Int?, screenTitle: String): String {
        return "$ROUTE_BASE/${id ?: "new"}/${screenTitle}"
    }
}
