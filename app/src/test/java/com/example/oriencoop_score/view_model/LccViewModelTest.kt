package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.model.Lcc
import com.example.oriencoop_score.model.LccResponse
import com.example.oriencoop_score.repository.LccRepository
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
class LccViewModelTest {

    // Executes LiveData tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use a test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LccViewModel
    private lateinit var repository: LccRepository
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
        viewModel = LccViewModel(repository, sessionManager)
        // Allow coroutines to run to completion
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        // Access error LiveData's value (assuming error is exposed as LiveData<String?>)
        assertEquals("Token o Rut no pueden estar vacíos", viewModel.error.value)
        // Verify that repository.getLcc was never called
        coVerify(exactly = 0) { repository.getLcc(any<String>(), any<String>()) }
    }

    // Test 2
    @Test
    fun `when repository returns success, lccDataData is updated`() = runTest {
        // Arrange

        // session manager already prepared in the setup

        // Prepare a dummy response for success
        val dummyResponse = LccResponse(
            lcc = listOf(
                Lcc(
                    CUPOAUTORIZADO = "500000.00",
                    CUPOUTILIZADO = "200000.00",
                    CUPODISPONIBLE = "300000.00",
                    NROCUENTA = 1234567890L
                )
            )
        )
        // Stub the suspend function repository.getLcc to return a successful result
        coEvery { repository.getLcc("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = LccViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(dummyResponse, viewModel.lccData.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    // Test 3
    @Test
    fun `when repository returns error, error LiveData is updated`() = runTest {
        // Arrange
        val exception = Exception("Error occurred")
        coEvery { repository.getLcc("tokenValue", "rutValue") } returns Result.Error(exception)

        // Act
        viewModel = LccViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Error occurred", viewModel.error.value)
        assertEquals(LccResponse(emptyList()), viewModel.lccData.value)
        viewModel.isLoading.value.let { assertFalse(it) }
    }

    // Test 4
    @Test
    fun `lccDataDatos calls repository with correct parameters`() = runTest {
        // Arrange
        val dummyResponse = LccResponse(
            lcc = listOf(
                Lcc(
                    CUPOAUTORIZADO = "500000.00",
                    CUPOUTILIZADO = "200000.00",
                    CUPODISPONIBLE = "300000.00",
                    NROCUENTA = 1234567890L
                )
            )
        )
        coEvery { repository.getLcc("tokenValue", "rutValue") } returns Result.Success(dummyResponse)

        // Act
        viewModel = LccViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // AAA: Assert
        coVerify { repository.getLcc("tokenValue", "rutValue") }
    }


    //Test 5
    @Test
    fun `loading state is updated correctly during data fetch`() = runTest {
        // Arrange
        val dummyResponse = LccResponse(
            lcc = listOf(
                Lcc(
                    CUPOAUTORIZADO = "500000.00",
                    CUPOUTILIZADO = "200000.00",
                    CUPODISPONIBLE = "300000.00",
                    NROCUENTA = 1234567890L
                )
            )
        )

        coEvery { repository.getLcc("tokenValue", "rutValue") } returns Result.Success(
            dummyResponse
        )
        // Act
        viewModel = LccViewModel(repository, sessionManager)

        // Observe loading states using Turbine
        viewModel.isLoading.test {
            // Initial state (false)
            val initialState = awaitItem()
            assertFalse(initialState)

            // Trigger data fetch (loading state should become true)
            viewModel.LccDatos()
            val loadingState = awaitItem()
            assertTrue(loadingState)

            // Final state (loading completes, state should return to false)
            val finalState = awaitItem()
            assertFalse(finalState)

            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }
    /*
    // Test 6
    @Test
    fun `when repository returns success with null data, lccData remains null`() = runTest {
        // Arrange
        coEvery { repository.getLcc("tokenValue", "rutValue") } returns Result.Success(
            LccResponse(null)
        )

        // Act
        viewModel = LccViewModel(repository, sessionManager)
        viewModel.LccDatos()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertNull(viewModel.lccData.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }*/

    // Test 7
    @Test
    fun `when repository returns loading, isLoading is set to true`() = runTest {
        // Arrange
        coEvery { repository.getLcc("tokenValue", "rutValue") } returns Result.Loading

        // Act
        viewModel = LccViewModel(repository, sessionManager)

        // Observe loading states using Turbine
        viewModel.isLoading.test {
            // Initial state (false)
            val initialState = awaitItem()
            assertFalse(initialState)

            // Trigger data fetch (loading state should become true)
            viewModel.LccDatos()
            val loadingState = awaitItem()
            assertTrue(loadingState)

            // Final state (loading completes, state should return to false)
            val finalState = awaitItem()
            assertFalse(finalState)

            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 8
    @Test
    fun `when repository throws an exception, error is set`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { repository.getLcc("tokenValue", "rutValue") } throws exception

        // Act
        viewModel = LccViewModel(repository, sessionManager)
        viewModel.LccDatos()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("Network error", viewModel.error.value)
        assertEquals(LccResponse(emptyList()), viewModel.lccData.value)
        assertFalse(viewModel.isLoading.value)
    }
}
