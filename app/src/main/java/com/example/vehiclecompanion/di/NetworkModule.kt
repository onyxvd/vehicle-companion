package com.example.vehiclecompanion.di

import com.example.vehiclecompanion.data.network.PlaceApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private const val BASE_URL = "https://api2.roadtrippers.com/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun providePlaceApiService(): PlaceApiService {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofitJson = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .addConverterFactory(retrofitJson.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(PlaceApiService::class.java)
    }
}
