package com.example.vehiclecompanion.data.repository

import com.example.vehiclecompanion.data.network.DiscoverResponse
import com.example.vehiclecompanion.data.network.Place
import com.example.vehiclecompanion.data.network.PlaceApiService
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val placeApiService: PlaceApiService
) {
    suspend fun discoverPlaces(
        swCorner: String = "-84.540499,39.079888",
        neCorner: String = "-84.494260,39.113254",
        pageSize: Int = 50
    ): DiscoverResponse {
        return placeApiService.discover(swCorner, neCorner, pageSize)
    }

    suspend fun getPlaceDetails(id: Int): Place {
        return placeApiService.getPlaceDetails(id)
    }
}
