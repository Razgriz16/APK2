package com.example.oriencoop_score.view_model

import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.MisProductosResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
import org.junit.Test
import com.example.oriencoop_score.utility.Result
import kotlinx.coroutines.delay

@OptIn(ExperimentalCoroutinesApi::class)
class MisProductosViewModelTest {

    // Mocks for repository and session manager
    private lateinit var repository: MisProductosRepository
    private lateinit var sessionManager: SessionManager

    // Using a TestDispatcher for coroutine testing
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = mockk()
        sessionManager = mockk()

        // Arrange: Provide test-specific token and username flows.
        every { sessionManager.token } returns MutableStateFlow("testToken")
        every { sessionManager.username } returns MutableStateFlow("testUser")

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `init calls obtenerProductos and sets loading state`() = runTest {
        // Arrange: Setup repository to return a dummy success result.
        val dummyResponse = MisProductosResponse(1, 1, 1, 1, 1, 1)
        coEvery { repository.getProductos(any(), any()) } returns Result.Success(dummyResponse)

        // Act: Instantiate the ViewModel (which triggers init -> obtenerProductos())
        val viewModel = MisProductosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all coroutines complete

        // Assert: Verify that repository.getProductos was called with expected values.
        coVerify { repository.getProductos("testToken", "testUser") }
        // Assert: Verify that the loading state ends as false.
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `obtenerProductos success updates productos and clears error`() = runTest {
        // Arrange: Prepare a response with mixed 1s and 0s.
        val dummyResponse = MisProductosResponse(1, 0, 1, 0, 1, 0)
        coEvery { repository.getProductos(any(), any()) } returns Result.Success(dummyResponse)
        val viewModel = MisProductosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: (Already executed in init)

        // Assert: Verify the products map conversion is correct.
        val expectedMap = mapOf(
            "AHORRO" to true,
            "CREDITO" to false,
            "CSOCIAL" to true,
            "DEPOSTO" to false,
            "LCC" to true,
            "LCR" to false
        )
        // AAA: Assert the productos map is correct.
        assertEquals(expectedMap, viewModel.productos.value)
        // AAA: Assert no error message is set.
        assertNull(viewModel.error.value)
        // AAA: Assert that the loading state is false at the end.
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `obtenerProductos error sets error message and clears productos`() = runTest {
        // Arrange: Setup the repository to return an error.
        val exceptionMessage = "Network error"
        coEvery { repository.getProductos(any(), any()) } returns Result.Error(Exception(exceptionMessage))
        val viewModel = MisProductosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: (The error is processed during init)

        // Assert: Verify the error message is set.
        // AAA: Assert that the error message matches.
        assertEquals(exceptionMessage, viewModel.error.value)
        // AAA: Assert that the productos map is cleared.
        assertTrue(viewModel.productos.value.isEmpty())
        // AAA: Assert that the loading state is false after processing.
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `isLoading state is correctly updated during successful operation`() = runTest {
        // Arrange: Simulate a delay in repository call to observe loading state.
        val dummyResponse = MisProductosResponse(1, 1, 0, 0, 1, 0)
        coEvery { repository.getProductos(any(), any()) } coAnswers {
            delay(1000) // Simulate network delay
            Result.Success(dummyResponse)
        }
        val viewModel = MisProductosViewModel(repository, sessionManager)

        // Act: Immediately after creation, the loading state should be true.
        // AAA: Assert initial loading state is true.
        assertTrue(viewModel.isLoading.value)

        // Advance time to let the delayed repository call complete.
        testDispatcher.scheduler.advanceTimeBy(150)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Verify that the loading state is now false.
        // AAA: Assert final loading state is false.
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `obtenerProductos with all fields zero returns all false in productos map`() = runTest {
        // Arrange: Prepare a response with all fields set to zero.
        val dummyResponse = MisProductosResponse(0, 0, 0, 0, 0, 0)
        coEvery { repository.getProductos(any(), any()) } returns Result.Success(dummyResponse)
        val viewModel = MisProductosViewModel(repository, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: (Conversion happens in init via obtenerProductos)

        // Assert: Verify that all values in the productos map are false.
        val expectedMap = mapOf(
            "CREDITO" to false,
            "AHORRO" to false,
            "DEPOSTO" to false,
            "LCC" to false,
            "LCR" to false,
            "CSOCIAL" to false
        )
        // AAA: Assert that the conversion produces the expected map.
        assertEquals(expectedMap, viewModel.productos.value)
    }
}
