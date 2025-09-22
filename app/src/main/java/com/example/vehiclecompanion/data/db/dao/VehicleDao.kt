package com.example.vehiclecompanion.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vehiclecompanion.data.db.model.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert
    suspend fun insertVehicle(vehicle: Vehicle): Long

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles WHERE id = :id")
    fun getVehicleById(id: Int): Flow<Vehicle?>

    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<Vehicle>>

    @Delete
    suspend fun deleteVehicle(vehicle: Vehicle)
}
