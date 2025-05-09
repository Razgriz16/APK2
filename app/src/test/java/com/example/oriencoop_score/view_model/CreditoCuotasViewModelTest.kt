package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.CreditoCuota
import com.example.oriencoop_score.model.CreditoCuotasResponse
import com.example.oriencoop_score.repository.CreditoCuotasRepository
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
class CreditoCuotasViewModelTest {

    // Executes LiveData tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use a test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CreditoCuotasViewModel
    private lateinit var repository: CreditoCuotasRepository
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
        viewModel = CreditoCuotasViewModel(repository, sessionManager)
        // Allow coroutines to run to completion
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        // Access error LiveData's value (assuming error is exposed as LiveData<String?>)
        assertEquals("Token o Rut no pueden estar vacíos", viewModel.error.value)
        // Verify that repository.getCreditoCuotas was never called
        coVerify(exactly = 0) { repository.getCreditoCuotas(any<String>(), any<String>()) }
    }

    // Test 2
    @Test
    fun `when repository returns success, creditoCuotasData is updated`() = runTest {
        // Arrange
        // Prepare a dummy response for success
        val dummyResponse = CreditoCuotasResponse(
            credito_cuotas = listOf(
                CreditoCuota(
                    MONTOCREDITO = "150000.00",
                    NROCUENTA = 1234567890L,
                    NUMEROCUOTAS = 12,
                    PROXVENCIMIENTO = "2025-03-15",
                    TIPOCUENTA = "Crédito Personal",
                    VALORCUOTA = "13500.50"
                ),
                CreditoCuota(
                    MONTOCREDITO = "50000.00",
                    NROCUENTA = 9876543210L,
                    NUMEROCUOTAS = 6,
                    PROXVENCIMIENTO = "2025-04-01",
                    TIPOCUENTA = "Crédito de Consumo",
                    VALORCUOTA = "8500.75"
                )
            )
        )
        // Stub the suspend function repository.getCreditoCuotas to return a successful result
        coEvery { repository.getCreditoCuotas("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CreditoCuotasViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse, viewModel.creditoCuotasData.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    // Test 3
    @Test
    fun `when repository returns error, error StateFlow is updated`() = runTest {
        // Arrange
        val exception = Exception("Error occurred")
        coEvery { repository.getCreditoCuotas("tokenValue", "rutValue") } returns Result.Error(exception)

        // Act
        viewModel = CreditoCuotasViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Error occurred", viewModel.error.value)
        assertEquals(CreditoCuotasResponse(credito_cuotas = emptyList()), viewModel.creditoCuotasData.value)
        viewModel.isLoading.value.let { assertFalse(it) }
    }

    // Test 4
    @Test
    fun `cuentaCapDatos calls repository with correct parameters`() = runTest {
        // Arrange
        val dummyResponse = CreditoCuotasResponse(
            credito_cuotas = listOf(
                CreditoCuota(
                    MONTOCREDITO = "150000.00",
                    NROCUENTA = 1234567890L,
                    NUMEROCUOTAS = 12,
                    PROXVENCIMIENTO = "2025-03-15",
                    TIPOCUENTA = "Crédito Personal",
                    VALORCUOTA = "13500.50"
                ),
                CreditoCuota(
                    MONTOCREDITO = "50000.00",
                    NROCUENTA = 9876543210L,
                    NUMEROCUOTAS = 6,
                    PROXVENCIMIENTO = "2025-04-01",
                    TIPOCUENTA = "Crédito de Consumo",
                    VALORCUOTA = "8500.75"
                )
            )
        )
        coEvery { repository.getCreditoCuotas("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CreditoCuotasViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        coVerify { repository.getCreditoCuotas("tokenValue", "rutValue") }
    }


    @Test
    fun `loading state is updated correctly during data fetch`() = runTest {
        // Arrange
        val dummyResponse = CreditoCuotasResponse(
            credito_cuotas = listOf(
                CreditoCuota(
                    MONTOCREDITO = "150000.00",
                    NROCUENTA = 1234567890L,
                    NUMEROCUOTAS = 12,
                    PROXVENCIMIENTO = "2025-03-15",
                    TIPOCUENTA = "Crédito Personal",
                    VALORCUOTA = "13500.50"
                ),
                CreditoCuota(
                    MONTOCREDITO = "50000.00",
                    NROCUENTA = 9876543210L,
                    NUMEROCUOTAS = 6,
                    PROXVENCIMIENTO = "2025-04-01",
                    TIPOCUENTA = "Crédito de Consumo",
                    VALORCUOTA = "8500.75"
                )
            )
        )
        coEvery { repository.getCreditoCuotas("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CreditoCuotasViewModel(repository, sessionManager)

        // Observe loading states using Turbine
        viewModel.isLoading.test {
            // Initial state (false)
            val initialState = awaitItem()
            assertFalse(initialState)

            // Trigger data fetch (loading state should become true)
            viewModel.creditoCuotasDatos()
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
    fun `selectCuenta updates cuentaSeleccionada when a new account is selected`() = runTest {
        // Arrange: Create two different dummy CreditoCuota objects.
        val account1 = CreditoCuota(
            MONTOCREDITO = "150000.00",
            NROCUENTA = 1234567890L,
            NUMEROCUOTAS = 12,
            PROXVENCIMIENTO = "2025-03-15",
            TIPOCUENTA = "Crédito Personal",
            VALORCUOTA = "13500.50"
        )
        val account2 = CreditoCuota(
            MONTOCREDITO = "50000.00",
            NROCUENTA = 9876543210L,
            NUMEROCUOTAS = 6,
            PROXVENCIMIENTO = "2025-04-01",
            TIPOCUENTA = "Crédito de Consumo",
            VALORCUOTA = "8500.75"
        )
        // Stub repository to return a dummy response containing both accounts.
        val dummyResponse = CreditoCuotasResponse(credito_cuotas = listOf(account1, account2))
        coEvery { repository.getCreditoCuotas("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act: Initialize the ViewModel and wait for initialization.
        viewModel = CreditoCuotasViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Select the first account.
        viewModel.selectCuenta(account1)

        // Assert: The selected account should now be account1.
        assertEquals(account1, viewModel.cuentaSeleccionada.value)
    }

    // Test 7
    @Test
    fun `selectCuenta toggles selection off when the same account is selected twice`() = runTest {
        // Arrange: Create a dummy CreditoCuota object.
        val account = CreditoCuota(
            MONTOCREDITO = "150000.00",
            NROCUENTA = 1234567890L,
            NUMEROCUOTAS = 12,
            PROXVENCIMIENTO = "2025-03-15",
            TIPOCUENTA = "Crédito Personal",
            VALORCUOTA = "13500.50"
        )
        // Stub repository to return a response with this account.
        val dummyResponse = CreditoCuotasResponse(credito_cuotas = listOf(account))
        coEvery { repository.getCreditoCuotas("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act: Initialize the ViewModel and wait for initialization.
        viewModel = CreditoCuotasViewModel(repository, sessionManager)
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
        coEvery { repository.getCreditoCuotas("tokenValue", "rutValue") } throws exception

        // Act
        viewModel = CreditoCuotasViewModel(repository, sessionManager)
        viewModel.creditoCuotasDatos()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Network error", viewModel.error.value)
        assertEquals(CreditoCuotasResponse(credito_cuotas = emptyList()), viewModel.creditoCuotasData.value)
        assertFalse(viewModel.isLoading.value)
    }


}
