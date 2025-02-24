package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.model.Movimiento
import com.example.oriencoop_score.model.MovimientosResponse
import com.example.oriencoop_score.repository.MovimientosRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.view_model.MovimientosViewModel
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Rule


@OptIn(ExperimentalCoroutinesApi::class)
class MovimientosViewModelTest {

    // Executes LiveData tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use a test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MovimientosViewModel
    private lateinit var repository: MovimientosRepository
    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        // Set the Main dispatcher to our test dispatcher
        Dispatchers.setMain(testDispatcher)
        // Create mocks for repository and sessionManager
        repository = mockk()
        sessionManager = mockk()
        // Stub sessionManager with default non-empty values (or empty, depending on the test)
        every { sessionManager.token } returns MutableStateFlow("tokenValue")
        every { sessionManager.username } returns MutableStateFlow("rutValue")
        // Mock Logs
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // Test 1
    @Test
    fun `when token or username is empty, error is set and repository is not called`() = runTest {
        // AAA: Arrange
        // Setup sessionManager to have an empty token to trigger the error condition.
        every { sessionManager.token } returns MutableStateFlow("")
        // The username can be non-empty
        every { sessionManager.username } returns MutableStateFlow("nonEmpty")

        // AAA: Act
        viewModel = MovimientosViewModel(repository, sessionManager)
        // Allow coroutines to run to completion
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        // Access error LiveData's value (assuming error is exposed as LiveData<String?>)
        assertEquals("Token o Rut no pueden estar vacíos", viewModel.error.value)
        // Verify that repository.getMovimientos was never called
        coVerify(exactly = 0) { repository.getMovimientos(any<String>(), any<String>()) }
    }

    // Test 2
    @Test
    fun `when repository returns success, Movimientos is updated`() = runTest {
        // Arrange

        // session manager already prepared in the setup

        // Prepare a dummy response for success
        val dummyResponse = MovimientosResponse(
            movimientos = listOf(
                Movimiento(
                    CUENTA = 1234567890L,
                    FECHAPAGO = "2025-02-15",
                    MONTO = "1500.75",
                    NOMBREABRTRANSACCION = "PAG",
                    NOMBRETRANSACCION = "Pago de Servicio",
                    ESCARGO = "N"
                ),
                Movimiento(
                    CUENTA = 9876543210L,
                    FECHAPAGO = "2025-02-20",
                    MONTO = "25000.00",
                    NOMBREABRTRANSACCION = "DEP",
                    NOMBRETRANSACCION = "Depósito Salarial",
                    ESCARGO = "S"
                )
            )
        )
        // Stub the suspend function repository.getMovimientos to return a successful result
        coEvery { repository.getMovimientos("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = MovimientosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse.movimientos, viewModel.movimientos.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    // Test 3
    @Test
    fun `when repository returns error, error StateFlow is updated`() = runTest {
        // Arrange
        val exception = Exception("Error occurred")
        coEvery { repository.getMovimientos("tokenValue", "rutValue") } returns Result.Error(exception)

        // Act
        viewModel = MovimientosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Error occurred", viewModel.error.value)
        assertEquals(MovimientosResponse(emptyList()).movimientos, viewModel.movimientos.value)
        viewModel.isLoading.value.let { assertFalse(it) }
    }

    // Test 4
    @Test
    fun `obtenerMovimientos calls repository with correct parameters`() = runTest {
        // Arrange
        val dummyResponse = MovimientosResponse(
            movimientos = listOf(
                Movimiento(
                    CUENTA = 1234567890L,
                    FECHAPAGO = "2025-02-15",
                    MONTO = "1500.75",
                    NOMBREABRTRANSACCION = "PAG",
                    NOMBRETRANSACCION = "Pago de Servicio",
                    ESCARGO = "N"
                ),
                Movimiento(
                    CUENTA = 9876543210L,
                    FECHAPAGO = "2025-02-20",
                    MONTO = "25000.00",
                    NOMBREABRTRANSACCION = "DEP",
                    NOMBRETRANSACCION = "Depósito Salarial",
                    ESCARGO = "S"
                )
            )
        )
        coEvery { repository.getMovimientos("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = MovimientosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        coVerify { repository.getMovimientos("tokenValue", "rutValue") }
    }


    //Test 5
    @Test
    fun `loading state is updated correctly during data fetch`() = runTest {
        // Arrange
        val dummyResponse = MovimientosResponse(
            movimientos = listOf(
                Movimiento(
                    CUENTA = 1234567890L,
                    FECHAPAGO = "2025-02-15",
                    MONTO = "1500.75",
                    NOMBREABRTRANSACCION = "PAG",
                    NOMBRETRANSACCION = "Pago de Servicio",
                    ESCARGO = "N"
                ),
                Movimiento(
                    CUENTA = 9876543210L,
                    FECHAPAGO = "2025-02-20",
                    MONTO = "25000.00",
                    NOMBREABRTRANSACCION = "DEP",
                    NOMBRETRANSACCION = "Depósito Salarial",
                    ESCARGO = "S"
                )
            )
        )

        coEvery { repository.getMovimientos("tokenValue", "rutValue") } returns Result.Success(
            dummyResponse
        )
        // Act
        viewModel = MovimientosViewModel(repository, sessionManager)

        // Observe loading states using Turbine
        viewModel.isLoading.test {
            // Initial state (false)
            val initialState = awaitItem()
            assertFalse(initialState)

            // Trigger data fetch (loading state should become true)
            viewModel.obtenerMovimientos()
            val loadingState = awaitItem()
            assertTrue(loadingState)

            // Final state (loading completes, state should return to false)
            val finalState = awaitItem()
            assertFalse(finalState)

            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }
}


