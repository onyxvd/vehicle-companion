package com.example.vehiclecompanion.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vehiclecompanion.data.db.dao.VehicleDao
import com.example.vehiclecompanion.data.db.model.Vehicle

@Database(entities = [Vehicle::class], version = 1, exportSchema = false)
abstract class VehicleCompanionDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}
