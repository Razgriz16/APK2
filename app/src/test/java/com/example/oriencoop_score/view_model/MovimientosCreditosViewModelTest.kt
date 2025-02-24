package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.model.MovimientosCreditosResponse
import com.example.oriencoop_score.repository.MovimientosCreditosRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MovimientosCreditosViewModelTest {

    // Executes LiveData tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use a test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MovimientosCreditosViewModel
    private lateinit var repository: MovimientosCreditosRepository
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
        viewModel = MovimientosCreditosViewModel(repository, sessionManager)
        // Allow coroutines to run to completion
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        // Access error LiveData's value (assuming error is exposed as LiveData<String?>)
        assertEquals("Token o Rut no pueden estar vacíos", viewModel.error.value)
        // Verify that repository.getMovimientosCreditos was never called
        coVerify(exactly = 0) { repository.getMovimientosCreditos(any<String>(), any<String>()) }
    }

    // Test 2
    @Test
    fun `when repository returns success, Movimientos is updated`() = runTest {
        // Arrange

        // session manager already prepared in the setup

        // Prepare a dummy response for success
        val dummyResponse = MovimientosCreditosResponse(
            movimientos_creditos = listOf(
                MovimientosCreditos(
                    FECHA = "2025-02-14",
                    MONTO = "8500.00",
                    NOMBRE = "Pago Mensual",
                    NROCUENTA = 1234567890L,
                    SUCURSAL = "Sucursal Centro"
                ),
                MovimientosCreditos(
                    FECHA = "2025-02-19",
                    MONTO = "15000.75",
                    NOMBRE = "Abono Extraordinario",
                    NROCUENTA = 9876543210L,
                    SUCURSAL = "Sucursal Norte"
                ),
                MovimientosCreditos(
                    FECHA = "2025-02-23",
                    MONTO = "3200.50",
                    NOMBRE = "Intereses",
                    NROCUENTA = 1234567890L,
                    SUCURSAL = "Sucursal Este"
                )
            )
        )
        // Stub the suspend function repository.getMovimientosCreditos to return a successful result
        coEvery { repository.getMovimientosCreditos("tokenValue", "rutValue") } returns Result.Success(
            dummyResponse
        )

        // Act
        viewModel = MovimientosCreditosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse.movimientos_creditos, viewModel.movimientos.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    // Test 3
    @Test
    fun `when repository returns error, error StateFlow is updated`() = runTest {
        // Arrange
        val exception = Exception("Error occurred")
        coEvery { repository.getMovimientosCreditos("tokenValue", "rutValue") } returns Result.Error(
            exception
        )

        // Act
        viewModel = MovimientosCreditosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Error occurred", viewModel.error.value)
        assertEquals(
            MovimientosCreditosResponse(emptyList()).movimientos_creditos,
            viewModel.movimientos.value
        )
        viewModel.isLoading.value.let { assertFalse(it) }
    }

    // Test 4
    @Test
    fun `obtenerMovimientos calls repository with correct parameters`() = runTest {
        // Arrange
        val dummyResponse = MovimientosCreditosResponse(
            movimientos_creditos = listOf(
                MovimientosCreditos(
                    FECHA = "2025-02-14",
                    MONTO = "8500.00",
                    NOMBRE = "Pago Mensual",
                    NROCUENTA = 1234567890L,
                    SUCURSAL = "Sucursal Centro"
                ),
                MovimientosCreditos(
                    FECHA = "2025-02-19",
                    MONTO = "15000.75",
                    NOMBRE = "Abono Extraordinario",
                    NROCUENTA = 9876543210L,
                    SUCURSAL = "Sucursal Norte"
                ),
                MovimientosCreditos(
                    FECHA = "2025-02-23",
                    MONTO = "3200.50",
                    NOMBRE = "Intereses",
                    NROCUENTA = 1234567890L,
                    SUCURSAL = "Sucursal Este"
                )
            )
        )
        coEvery { repository.getMovimientosCreditos("tokenValue", "rutValue") } returns Result.Success(
            dummyResponse
        )

        // Act
        viewModel = MovimientosCreditosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        coVerify { repository.getMovimientosCreditos("tokenValue", "rutValue") }
    }


    //Test 5
    @Test
    fun `loading state is updated correctly during data fetch`() = runTest {
        // Arrange
        val dummyResponse = MovimientosCreditosResponse(
            movimientos_creditos = listOf(
                MovimientosCreditos(
                    FECHA = "2025-02-14",
                    MONTO = "8500.00",
                    NOMBRE = "Pago Mensual",
                    NROCUENTA = 1234567890L,
                    SUCURSAL = "Sucursal Centro"
                ),
                MovimientosCreditos(
                    FECHA = "2025-02-19",
                    MONTO = "15000.75",
                    NOMBRE = "Abono Extraordinario",
                    NROCUENTA = 9876543210L,
                    SUCURSAL = "Sucursal Norte"
                ),
                MovimientosCreditos(
                    FECHA = "2025-02-23",
                    MONTO = "3200.50",
                    NOMBRE = "Intereses",
                    NROCUENTA = 1234567890L,
                    SUCURSAL = "Sucursal Este"
                )
            )
        )
        coEvery { repository.getMovimientosCreditos("tokenValue", "rutValue") } returns Result.Success(
            dummyResponse
        )
        // Act
        viewModel = MovimientosCreditosViewModel(repository, sessionManager)

        // Observe loading states using Turbine
        viewModel.isLoading.test {
            // Initial state (false)
            val initialState = awaitItem()
            assertFalse(initialState)

            // Trigger data fetch (loading state should become true)
            viewModel.obtenerMovimientosCreditos()
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