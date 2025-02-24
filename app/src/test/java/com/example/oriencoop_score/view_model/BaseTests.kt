package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.oriencoop_score.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.ui.theme.amarillo
import junit.framework.TestCase.assertTrue
import kotlin.coroutines.Continuation
/*
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTests<
        VM : Any,
        R : Any,
        Response : Any,
        Entity : Any
        > {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    protected val testDispatcher = StandardTestDispatcher()
    protected lateinit var repository: R
    protected lateinit var sessionManager: SessionManager
    protected lateinit var viewModel: VM

    // Abstract properties to be implemented by subclasses
    abstract val repositoryFunction: suspend R.(String, String) -> Result<Response>
    abstract val emptyResponse: Response
    abstract val successResponse: Response
    abstract val errorMessage: String
    abstract val expectedSuccessEntities: List<Entity>

    // BaseTests.kt
    @Before
    open fun setup() {
        Dispatchers.setMain(testDispatcher)
        sessionManager = mockk {
            every { token } returns MutableStateFlow("tokenValue")
            every { username } returns MutableStateFlow("rutValue")
        }
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        repository = createRepository()

        // Mock the repository function without specifying the continuation parameter
        coEvery {
            repository.repositoryFunction(any<String>(), any<String>())
        } returns Result.Success(emptyResponse)

        viewModel = createViewModel()
    }

    @After
    open fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    abstract fun createViewModel(): VM
    abstract fun createRepository(): R
    abstract fun getErrorState(): StateFlow<String?>
    abstract fun getDataState(): StateFlow<List<Entity>>
    abstract fun getLoadingState(): StateFlow<Boolean>
    abstract fun triggerDataFetch()
    abstract fun Response.toEntityList(): List<Entity>

    //-------------------
    // YOUR ORIGINAL TESTS
    //-------------------

    @Test
    fun `when token or username is empty, error is set and repository is not called`() = runTest {
        every { sessionManager.token } returns MutableStateFlow("")
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(errorMessage, getErrorState().value)

        // Verify the repository function is not called
        coVerify(exactly = 0) {
            repository.repositoryFunction(any<String>(), any<String>())
        }
    }
    @Test
    fun `when repository returns success, data is updated`() = runTest {
        // Stub with Continuation
        coEvery {
            repository.repositoryFunction(any<String>(), any<String>())
        } returns Result.Success(successResponse)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expectedSuccessEntities, getDataState().value)
    }

    @Test
    fun `when repository returns error, error is updated`() = runTest {
        val exception = Exception("Error occurred")
        coEvery { repository.repositoryFunction(any<String>(), any<String>()) } returns Result.Error(exception)
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Error occurred", getErrorState().value)
        assertEquals(emptyResponse.toEntityList(), getDataState().value)
        assertFalse(getLoadingState().value)
    }

    // Inside BaseTests.kt
    @Test
    fun `repository called with correct parameters`() = runTest {
        // Stub with explicit parameters (including Continuation)
        coEvery {
            repository.repositoryFunction(eq("tokenValue"), eq("rutValue"))
        } returns Result.Success(successResponse)

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify with Continuation
        coVerify {
            repository.repositoryFunction(eq("tokenValue"), eq("rutValue"))
        }
    }

    // Example test in BaseTests
    @Test
    fun `loading state updates correctly`() = runTest {
        coEvery { repository.repositoryFunction(any<String>(), any<String>()) } returns Result.Success(successResponse)
        viewModel = createViewModel()

        getLoadingState().test {
            // Initial state (false)
            assertEquals(false, awaitItem())

            // Trigger data fetch
            triggerDataFetch()
            testDispatcher.scheduler.advanceUntilIdle() // <-- Advance here

            // Verify loading states
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}*/