package com.example.vehiclecompanion.ui.vehicle

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.vehiclecompanion.data.db.model.Vehicle
import com.example.vehiclecompanion.data.repository.VehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class VehicleDetailsViewModelTest {

    private lateinit var viewModel: VehicleDetailsViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private val vehicleRepository: VehicleRepository = mock()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun initViewModel(vehicleId: String?) {
        savedStateHandle = SavedStateHandle().apply {
            if (vehicleId != null) {
                set("vehicleId", vehicleId)
            }
        }

        // Only set up the "new" or "null" case mock within initViewModel.
        // Tests for existing vehicle IDs are responsible for setting up their own
        // vehicleRepository.getVehicle(id) mocks.
        if (vehicleId == null || vehicleId == "new") {
            whenever(vehicleRepository.getVehicle(any())).thenReturn(flowOf(null))
        }

        viewModel = VehicleDetailsViewModel(vehicleRepository, savedStateHandle)

        testDispatcher.scheduler.advanceUntilIdle()

    }

    @Test
    fun testSaveVehicleWithValidData() = runTest {
        initViewModel("new")

        // Fill required fields
        viewModel.updateMake("Honda")
        viewModel.updateModel("Civic")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.saveEventChannel.test {
            viewModel.saveVehicle()
            testDispatcher.scheduler.advanceUntilIdle()

            val successState =
                viewModel.vehicleUiState.value as? VehicleDetailsScreenUiState.Success
            assertNotNull(successState)
            assertNull(successState?.formError)

            val vehicleCaptor = argumentCaptor<Vehicle>()
            verify(vehicleRepository).insertVehicle(vehicleCaptor.capture())

            assertEquals("Honda", vehicleCaptor.firstValue.make)
            assertEquals("Civic", vehicleCaptor.firstValue.model)

            // New vehicle should not have an ID
            assertNull(vehicleCaptor.firstValue.id)

            // Verify that save event was emitted
            assertEquals(Unit, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun testSaveVehicleWithMissingRequiredFields() = runTest {
        initViewModel("new")

        viewModel.updateMake("Honda")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.saveEventChannel.test {
            viewModel.saveVehicle()
            testDispatcher.scheduler.advanceUntilIdle()

            val successState =
                viewModel.vehicleUiState.value as? VehicleDetailsScreenUiState.Success
            assertNotNull(successState)
            assertNotNull(successState?.formError)
            assertEquals("Make and Model cannot be empty", successState?.formError)

            // Verify that insertVehicle was never called
            verify(vehicleRepository, never()).insertVehicle(any())

            // Verify that no save event was emitted
            expectNoEvents()
        }
    }

    @Test
    fun testUpdateVehicleFields() = runTest {
        val existingVehicle = Vehicle(
            id = 1,
            name = "My Car",
            make = "Toyota",
            model = "Camry",
            year = 2020,
            vin = "1234567890ABCDEFG",
            fuelType = "Gasoline"
        )

        whenever(vehicleRepository.getVehicle(1)).thenReturn(flowOf(existingVehicle))

        initViewModel("1")

        val successState =
            viewModel.vehicleUiState.value as? VehicleDetailsScreenUiState.Success
        assertNotNull(successState)
        assertEquals("Toyota", successState?.vehicleData?.make)
        assertEquals("Camry", successState?.vehicleData?.model)
        assertEquals(2020, successState?.vehicleData?.year)
        assertEquals("1234567890ABCDEFG", successState?.vehicleData?.vin)
        assertEquals("Gasoline", successState?.vehicleData?.fuelType)

        // Update fields
        viewModel.updateMake("Honda")
        viewModel.updateModel("Accord")
        viewModel.updateYear(2021)
        viewModel.updateVin("0987654321HGFEDCBA")
        viewModel.updateFuelType("Hybrid")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.saveEventChannel.test {
            viewModel.saveVehicle()
            testDispatcher.scheduler.advanceUntilIdle()

            val vehicleCaptor = argumentCaptor<Vehicle>()
            verify(vehicleRepository).updateVehicle(vehicleCaptor.capture())
            assertEquals(1, vehicleCaptor.firstValue.id)
            assertEquals("Honda", vehicleCaptor.firstValue.make)
            assertEquals("Accord", vehicleCaptor.firstValue.model)
            assertEquals(2021, vehicleCaptor.firstValue.year)
            assertEquals("0987654321HGFEDCBA", vehicleCaptor.firstValue.vin)
            assertEquals("Hybrid", vehicleCaptor.firstValue.fuelType)

            val updatedState =
                viewModel.vehicleUiState.value as? VehicleDetailsScreenUiState.Success
            assertNotNull(updatedState)

            // Verify that save event was emitted
            assertEquals(Unit, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDeleteVehicle() = runTest {
        val existingVehicle = Vehicle(
            id = 1,
            name = "My Car",
            make = "Toyota",
            model = "Camry",
            year = 2020,
            vin = "1234567890ABCDEFG",
            fuelType = "Gasoline"
        )

        whenever(vehicleRepository.getVehicle(1)).thenReturn(flowOf(existingVehicle))

        initViewModel("1")

        val initialSuccessState =
            viewModel.vehicleUiState.value as? VehicleDetailsScreenUiState.Success
        assertNotNull(initialSuccessState)
        assertEquals(1, initialSuccessState?.vehicleData?.id)

        viewModel.deleteEventChannel.test {
            viewModel.deleteVehicle()
            testDispatcher.scheduler.advanceUntilIdle()

            val vehicleCaptor = argumentCaptor<Vehicle>()
            verify(vehicleRepository).deleteVehicle(vehicleCaptor.capture())
            assertEquals(1, vehicleCaptor.firstValue.id)

            // Verify that delete event was emitted
            assertEquals(Unit, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
