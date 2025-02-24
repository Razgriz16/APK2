package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.api.CuentaCapService
import com.example.oriencoop_score.api.MisProductosService
import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.model.MisProductosResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class MisProductosRepositoryTest {

    // Dependencies to be mocked and the class under test
    private lateinit var misProductosService: MisProductosService
    private lateinit var sessionManager: SessionManager

    @Before
    fun setup() {
        // Initialize MockK annotations and create mocks
        MockKAnnotations.init(this)
        misProductosService = mockk()
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
        unmockkAll()
    }

    @Test
    fun `getCuentaCap returns success when service responds with data`() = runTest {
        // Arrange
        val expectedResponse = MisProductosResponse(
            AHORRO = 1,
            CREDITO = 1,
            CSOCIAL = 1,
            DEPOSTO = 0,
            LCC = 1,
            LCR = 1
        )
        coEvery { misProductosService.getProductos(any<String>(), any<String>()) } returns Response.success(expectedResponse)

        // Act
        val repository = MisProductosRepository(misProductosService)
        val result = repository.getProductos("tokenValue", "rutValue")

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, (result as Result.Success).data)
        coVerify { misProductosService.getProductos("tokenValue", "rutValue") }
    }

    @Test
    fun `getCuentaCap returns error when service responds with successful but null body`() = runTest {
        // Arrange

        val mockResponse = mockk<Response<MisProductosResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null
        coEvery { misProductosService.getProductos(any<String>(), any<String>()) } returns mockResponse

        // Act
        val repository = MisProductosRepository(misProductosService)
        val result = repository.getProductos("tokenValue", "rutValue")

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(null, (result as Result.Error).exception.message)
        coVerify { misProductosService.getProductos("tokenValue", "rutValue") }
    }

    @Test
    fun `getCuentaCap returns error when service responds with unsuccessful response`() = runTest {
        // Arrange

        val mockResponse = mockk<Response<MisProductosResponse>>()
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 404
        every { mockResponse.message() } returns "Not Found"
        coEvery { misProductosService.getProductos(any<String>(), any<String>()) } returns mockResponse

        // Act
        val repository = MisProductosRepository(misProductosService)
        val result = repository.getProductos("tokenValue", "rutValue")

        // Assert
        assertTrue(result is Result.Error)
        val errorMessage = (result as Result.Error).exception.message
        assertTrue(errorMessage?.contains("404") == true)
        assertTrue(errorMessage?.contains("Not Found") == true)
        coVerify { misProductosService.getProductos("tokenValue", "rutValue") }
    }

    @Test
    fun `getCuentaCap returns error when service throws exception`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val exception = IOException("Network failure")
        coEvery { misProductosService.getProductos(any<String>(), any<String>()) } throws exception

        // Act
        val repository = MisProductosRepository(misProductosService)
        val result = repository.getProductos("tokenValue", "rutValue")

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify { misProductosService.getProductos("tokenValue", "rutValue") }
    }

    @Test
    fun `getCuentaCap calls service with correct parameters`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        coEvery { misProductosService.getProductos(any<String>(), any<String>()) } returns mockk()

        // Act
        val repository = MisProductosRepository(misProductosService)
        repository.getProductos("tokenValue", "rutValue")

        // Assert
        coVerify { misProductosService.getProductos("tokenValue", "rutValue") }
    }
}