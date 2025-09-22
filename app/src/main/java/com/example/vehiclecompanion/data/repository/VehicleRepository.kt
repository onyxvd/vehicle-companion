package com.example.vehiclecompanion.data.repository

import com.example.vehiclecompanion.data.db.dao.VehicleDao
import com.example.vehiclecompanion.data.db.model.Vehicle
import javax.inject.Inject

class VehicleRepository @Inject constructor(
    private val vehicleDao: VehicleDao
) {
    fun getAllVehicles() = vehicleDao.getAllVehicles()

    fun getVehicle(id: Int) = vehicleDao.getVehicleById(id)

    suspend fun insertVehicle(vehicle: Vehicle) =
        vehicleDao.insertVehicle(vehicle)

    suspend fun updateVehicle(vehicle: Vehicle) =
        vehicleDao.updateVehicle(vehicle)
}
