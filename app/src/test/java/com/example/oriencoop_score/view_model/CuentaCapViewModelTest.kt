package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.repository.CuentaCapRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
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
import com.example.oriencoop_score.utility.Result
import io.mockk.unmockkAll
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class CuentaCapViewModelTest {

    // Executes LiveData tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use a test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CuentaCapViewModel
    private lateinit var repository: CuentaCapRepository
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
        viewModel = CuentaCapViewModel(repository, sessionManager)
        // Allow coroutines to run to completion
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        // Access error LiveData's value (assuming error is exposed as LiveData<String?>)
        assertEquals("Token o Rut no pueden estar vacíos", viewModel.error.value)
        // Verify that repository.getCuentaCap was never called
        coVerify(exactly = 0) { repository.getCuentaCap(any<String>(), any<String>()) }
    }

    // Test 2
    @Test
    fun `when repository returns success, cuentaCapData is updated`() = runTest {
        // Arrange

        // session manager already prepared in the setup

        // Prepare a dummy response for success
        val dummyResponse = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro")
        // Stub the suspend function repository.getCuentaCap to return a successful result
        coEvery { repository.getCuentaCap("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CuentaCapViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse, viewModel.cuentaCapData.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    // Test 3
    @Test
    fun `when repository returns error, error LiveData is updated`() = runTest {
        // Arrange
        val exception = Exception("Error occurred")
        coEvery { repository.getCuentaCap("tokenValue", "rutValue") } returns Result.Error(exception)

        // Act
        viewModel = CuentaCapViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Error occurred", viewModel.error.value)
        assertNull(viewModel.cuentaCapData.value)
        viewModel.isLoading.value.let { assertFalse(it) }
    }

    // Test 4
    @Test
    fun `cuentaCapDatos calls repository with correct parameters`() = runTest {
        // Arrange
        val dummyResponse = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro")
        coEvery { repository.getCuentaCap("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = CuentaCapViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        coVerify { repository.getCuentaCap("tokenValue", "rutValue") }
    }


    //Test 5
    @Test
    fun `loading state is updated correctly during data fetch`() = runTest {
        // Arrange
        val dummyResponse = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro"
        )

        coEvery { repository.getCuentaCap("tokenValue", "rutValue") } returns Result.Success(
            dummyResponse
        )
        // Act
        viewModel = CuentaCapViewModel(repository, sessionManager)

        // Observe loading states using Turbine
        viewModel.isLoading.test {
            // Initial state (false)
            val initialState = awaitItem()
            assertFalse(initialState)

            // Trigger data fetch (loading state should become true)
            viewModel.cuentaCapDatos()
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
    fun `cuentaCapDatos handles multiple sequential calls correctly`() = runTest {
        // Arrange
        val dummyResponse1 = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro"
        )
        val dummyResponse2 = CuentaCapResponse(
            FECHAAPERTURA = "2023-02-01",
            NROCUENTA = 987654321L,
            SALDOCONTABLE = "2000.00",
            TIPOCUENTA = "Corriente"
        )
        coEvery { repository.getCuentaCap("tokenValue", "rutValue") } returns Result.Success(dummyResponse1) andThen Result.Success(dummyResponse2)

        // Act
        viewModel = CuentaCapViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse1, viewModel.cuentaCapData.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)

        // Act: Call cuentaCapDatos again
        viewModel.cuentaCapDatos()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse2, viewModel.cuentaCapData.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    // Test 7
    @Test
    fun `when repository returns Loading, no data or error is updated`() = runTest {
        // Arrange: Stub repository to return Result.Loading.
        coEvery { repository.getCuentaCap("tokenValue", "rutValue") } returns Result.Loading

        // Act: Initialize the ViewModel.
        viewModel = CuentaCapViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert:
        // - Final loading state should be false (after the API call completes).
        // - Neither data nor error should be updated.
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
        assertNull(viewModel.cuentaCapData.value)
    }

    // Test 8
    @Test
    fun `when repository throws an exception, error is set`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { repository.getCuentaCap("tokenValue", "rutValue") } throws exception

        // Act
        viewModel = CuentaCapViewModel(repository, sessionManager)
        viewModel.cuentaCapDatos()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Network error", viewModel.error.value)
        assertNull(viewModel.cuentaCapData.value)
        assertFalse(viewModel.isLoading.value)
    }

}
