package com.example.vehiclecompanion.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String?,
    val make: String,
    val model: String,
    val year: Int?,
    val vin: String?,
    val fuelType: String?,
)
