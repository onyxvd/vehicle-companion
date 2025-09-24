package com.example.vehiclecompanion.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceApiService {
    @GET("/api/v2/pois/discover")
    suspend fun discover(
        @Query("sw_corner") swCorner: String,
        @Query("ne_corner") neCorner: String,
        @Query("page_size") pageSize: Int
    ): DiscoverResponse

    @GET("api/v1/pois/{id}")
    suspend fun getPlaceDetails(@Path("id") id: Int): Place
}
