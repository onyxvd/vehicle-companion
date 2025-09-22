package com.example.vehiclecompanion.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceApiService {
    @GET("/api/v2/pois/discover")
    suspend fun discover(
        @Query("sw_corner") swCorner: String = "-84.540499,39.079888",
        @Query("ne_corner") neCorner: String = "-84.494260,39.113254",
        @Query("page_size") pageSize: Int = 50
    ): DiscoverResponse
}
