package com.example.vehiclecompanion.data.repository

import com.example.vehiclecompanion.data.db.dao.VehicleDao
import com.example.vehiclecompanion.data.db.model.Vehicle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleRepositoryTest {

    val testVehicle = Vehicle(
        id = 1,
        name = "My Car",
        make = "Toyota",
        model = "Camry",
        year = 2020,
        vin = "1HGBH41JXMN109186",
        fuelType = "Gasoline",
    )

    val mockDao = mock<VehicleDao> {
        whenever(it.getAllVehicles()).thenReturn(
            flow {
                emit(
                    listOf(
                        testVehicle
                    )
                )
            }
        )
        whenever(it.getVehicleById(1)).thenReturn(
            flow {
                emit(testVehicle)
            }
        )
    }

    val vehicleRepository = VehicleRepository(
        vehicleDao = mockDao
    )

    @Test
    fun testCreateVehicle() = runTest {
        vehicleRepository.insertVehicle(testVehicle)
        advanceUntilIdle()

        verify(
            mock = mockDao
        ).insertVehicle(testVehicle)
    }

    @Test
    fun testGetAllVehicles() = runTest {
        val items = vehicleRepository.getAllVehicles().toList().first()
        advanceUntilIdle()

        val resultVehicle = items[0]
        Assert.assertEquals(
            resultVehicle, Vehicle(
                id = 1,
                name = "My Car",
                make = "Toyota",
                model = "Camry",
                year = 2020,
                vin = "1HGBH41JXMN109186",
                fuelType = "Gasoline",
            )
        )
    }

    @Test
    fun testGetVehicle() = runTest {
        val vehicle = vehicleRepository.getVehicle(1).toList().first()
        advanceUntilIdle()

        verify(
            mock = mockDao
        ).getVehicleById(1)

        Assert.assertEquals(vehicle, testVehicle)
    }

    @Test
    fun testUpdateVehicle() = runTest {
        vehicleRepository.updateVehicle(testVehicle)
        advanceUntilIdle()

        verify(
            mock = mockDao
        ).updateVehicle(testVehicle)
    }

    @Test
    fun testDeleteVehicle() = runTest {
        vehicleRepository.deleteVehicle(testVehicle)
        advanceUntilIdle()
        verify(
            mock = mockDao
        ).deleteVehicle(testVehicle)

    }
}
