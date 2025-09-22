package com.example.vehiclecompanion.di

import android.content.Context
import androidx.room.Room
import com.example.vehiclecompanion.data.db.VehicleCompanionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VehicleCompanionDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = VehicleCompanionDatabase::class.java,
            name = "vehicle_companion_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideVehicleDao(database: VehicleCompanionDatabase) = database.vehicleDao()


}
