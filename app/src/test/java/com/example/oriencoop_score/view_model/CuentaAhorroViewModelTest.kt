package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.CuentaAhorroResponse
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.repository.CuentaAhorroRepository
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
class CuentaAhorroViewModelTest {

    // Executes LiveData tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use a test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CuentaAhorroViewModel
    private lateinit var repository: CuentaAhorroRepository
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
        viewModel = CuentaAhorroViewModel(repository, sessionManager)
        // Allow coroutines to run to completion
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        // Access error LiveData's value (assuming error is exposed as LiveData<String?>)
        assertEquals("Token o Rut no pueden estar vacíos", viewModel.error.value)
        // Verify that repository.getAhorro was never called
        coVerify(exactly = 0) { repository.getAhorro(any<String>(), any<String>()) }
    }

    // Test 2
    @Test
    fun `when repository returns success, cuentaAhorroData is updated`() = runTest {
        // Arrange
        // Prepare a dummy response for success
        val dummyResponse = CuentaAhorroResponse(
            ahorro = listOf(
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Básico",
                    SALDODISPONIBLE = "45000.75",
                    SALDOCONTABLE = "50000.00",
                    SUCURSAL = "Sucursal Centro",
                    FECHAAPERTURA = "2023-06-15",
                    NROCUENTA = 1234567890L
                ),
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Premium",
                    SALDODISPONIBLE = "120000.50",
                    SALDOCONTABLE = "125000.00",
                    SUCURSAL = "Sucursal Norte",
                    FECHAAPERTURA = "2022-11-01",
                    NROCUENTA = 9876543210L
                )
            )
        )
        // Stub the suspend function repository.getAhorro to return a successful result
        coEvery { repository.getAhorro("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CuentaAhorroViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse, viewModel.cuentaAhorroData.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    // Test 3
    @Test
    fun `when repository returns error, error StateFlow is updated`() = runTest {
        // Arrange
        val exception = Exception("Error occurred")
        coEvery { repository.getAhorro("tokenValue", "rutValue") } returns Result.Error(exception)

        // Act
        viewModel = CuentaAhorroViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Error occurred", viewModel.error.value)
        assertEquals(CuentaAhorroResponse(ahorro = emptyList()), viewModel.cuentaAhorroData.value)
        viewModel.isLoading.value.let { assertFalse(it) }
    }

    // Test 4
    @Test
    fun `cuentaAhorro calls repository with correct parameters`() = runTest {
        // Arrange
        val dummyResponse = CuentaAhorroResponse(
            ahorro = listOf(
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Básico",
                    SALDODISPONIBLE = "45000.75",
                    SALDOCONTABLE = "50000.00",
                    SUCURSAL = "Sucursal Centro",
                    FECHAAPERTURA = "2023-06-15",
                    NROCUENTA = 1234567890L
                ),
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Premium",
                    SALDODISPONIBLE = "120000.50",
                    SALDOCONTABLE = "125000.00",
                    SUCURSAL = "Sucursal Norte",
                    FECHAAPERTURA = "2022-11-01",
                    NROCUENTA = 9876543210L
                )
            )
        )
        coEvery { repository.getAhorro("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CuentaAhorroViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        coVerify { repository.getAhorro("tokenValue", "rutValue") }
    }

    // Test 5
    @Test
    fun `loading state is updated correctly during data fetch`() = runTest {
        // Arrange
        val dummyResponse = CuentaAhorroResponse(
            ahorro = listOf(
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Básico",
                    SALDODISPONIBLE = "45000.75",
                    SALDOCONTABLE = "50000.00",
                    SUCURSAL = "Sucursal Centro",
                    FECHAAPERTURA = "2023-06-15",
                    NROCUENTA = 1234567890L
                ),
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Premium",
                    SALDODISPONIBLE = "120000.50",
                    SALDOCONTABLE = "125000.00",
                    SUCURSAL = "Sucursal Norte",
                    FECHAAPERTURA = "2022-11-01",
                    NROCUENTA = 9876543210L
                )
            )
        )
        coEvery { repository.getAhorro("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CuentaAhorroViewModel(repository, sessionManager)

        // Observe loading states using Turbine
        viewModel.isLoading.test {
            // Initial state (false)
            val initialState = awaitItem()
            assertFalse(initialState)

            // Trigger data fetch (loading state should become true)
            viewModel.cuentaAhorroDatos()
            val loadingState = awaitItem()
            assertTrue(loadingState)

            // Final state (loading completes, state should return to false)
            val finalState = awaitItem()
            assertFalse(finalState)

            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 6
    @Test
    fun `selectCuenta toggles the selected account correctly`() = runTest {
        // Arrange
        val cuenta1 = CuentaAhorro(
            TIPOCUENTA = "Ahorro Básico",
            SALDODISPONIBLE = "45000.75",
            SALDOCONTABLE = "50000.00",
            SUCURSAL = "Sucursal Centro",
            FECHAAPERTURA = "2023-06-15",
            NROCUENTA = 1234567890L
        )
        val cuenta2 = CuentaAhorro(
            TIPOCUENTA = "Ahorro Premium",
            SALDODISPONIBLE = "120000.50",
            SALDOCONTABLE = "125000.00",
            SUCURSAL = "Sucursal Norte",
            FECHAAPERTURA = "2022-11-01",
            NROCUENTA = 9876543210L
        )

        // Act
        viewModel = CuentaAhorroViewModel(repository, sessionManager)

        // Select cuenta1
        viewModel.selectCuenta(cuenta1)
        assertEquals(cuenta1, viewModel.cuentaSeleccionada.value)

        // Select cuenta1 again (should unselect it)
        viewModel.selectCuenta(cuenta1)
        assertNull(viewModel.cuentaSeleccionada.value)

        // Select cuenta2
        viewModel.selectCuenta(cuenta2)
        assertEquals(cuenta2, viewModel.cuentaSeleccionada.value)
    }

    // Test 7
    @Test
    fun `selectCuenta toggles selection off when the same account is selected twice`() = runTest {
        // Arrange: Create a dummy CreditoCuota object.
        val account = CuentaAhorro(
            TIPOCUENTA = "Ahorro Premium",
            SALDODISPONIBLE = "120000.50",
            SALDOCONTABLE = "125000.00",
            SUCURSAL = "Sucursal Norte",
            FECHAAPERTURA = "2022-11-01",
            NROCUENTA = 9876543210L
        )
        // Stub repository to return a response with this account.
        val dummyResponse = CuentaAhorroResponse(ahorro = listOf(account))
        coEvery { repository.getAhorro("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act: Initialize the ViewModel and wait for initialization.
        viewModel = CuentaAhorroViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // First selection should set the account.
        viewModel.selectCuenta(account)
        assertEquals(account, viewModel.cuentaSeleccionada.value)

        // Second selection (same account) should toggle and clear the selection.
        viewModel.selectCuenta(account)
        assertNull(viewModel.cuentaSeleccionada.value)
    }

    // Test 8
    @Test
    fun `when repository throws an exception, error is set`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { repository.getAhorro("tokenValue", "rutValue") } throws exception

        // Act
        viewModel = CuentaAhorroViewModel(repository, sessionManager)
        viewModel.cuentaAhorroDatos()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Network error", viewModel.error.value)
        assertEquals(CuentaAhorroResponse(ahorro = emptyList()), viewModel.cuentaAhorroData.value)
        assertFalse(viewModel.isLoading.value)
    }

}
