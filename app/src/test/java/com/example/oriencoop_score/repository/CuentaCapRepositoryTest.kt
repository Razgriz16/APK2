package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.MisProductosService
import com.example.oriencoop_score.model.CuentaCapResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import java.io.IOException

@ExperimentalCoroutinesApi
class CuentaCapRepositoryTest {

    // Dependencies to be mocked and the class under test
    private lateinit var cuentaCapService: MisProductosService

    @Before
    fun setup() {
        // Initialize MockK annotations and create mocks
        MockKAnnotations.init(this)
        cuentaCapService = mockk()

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
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val expectedResponse = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro")
        coEvery { cuentaCapService.getCuentaCap(token, rut) } returns Response.success(expectedResponse)

        // Act
        val repository = CuentaCapRepository(cuentaCapService)
        val result = repository.getCuentaCap(token, rut)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, (result as Result.Success).data)
        coVerify { cuentaCapService.getCuentaCap(token, rut) }
    }

    @Test
    fun `getCuentaCap returns error when service responds with successful but null body`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<CuentaCapResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null
        coEvery { cuentaCapService.getCuentaCap(token, rut) } returns mockResponse

        // Act
        val repository = CuentaCapRepository(cuentaCapService)
        val result = repository.getCuentaCap(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("Respuesta vacía", (result as Result.Error).exception.message)
        coVerify { cuentaCapService.getCuentaCap(token, rut) }
    }

    @Test
    fun `getCuentaCap returns error when service responds with unsuccessful response`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<CuentaCapResponse>>()
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 404
        every { mockResponse.message() } returns "Not Found"
        coEvery { cuentaCapService.getCuentaCap(token, rut) } returns mockResponse

        // Act
        val repository = CuentaCapRepository(cuentaCapService)
        val result = repository.getCuentaCap(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        val errorMessage = (result as Result.Error).exception.message
        assertTrue(errorMessage?.contains("404") == true)
        assertTrue(errorMessage?.contains("Not Found") == true)
        coVerify { cuentaCapService.getCuentaCap(token, rut) }
    }

    @Test
    fun `getCuentaCap returns error when service throws exception`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val exception = IOException("Network failure")
        coEvery { cuentaCapService.getCuentaCap(token, rut) } throws exception

        // Act
        val repository = CuentaCapRepository(cuentaCapService)
        val result = repository.getCuentaCap(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify { cuentaCapService.getCuentaCap(token, rut) }
    }

    @Test
    fun `getCuentaCap calls service with correct parameters`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        coEvery { cuentaCapService.getCuentaCap(token, rut) } returns mockk()

        // Act
        val repository = CuentaCapRepository(cuentaCapService)
        repository.getCuentaCap(token, rut)

        // Assert
        coVerify { cuentaCapService.getCuentaCap(token, rut) }
    }
}

/*
@ExperimentalCoroutinesApi
class CuentaCapRepositoryTest {

    // Mock dependencies
    @Mock
    lateinit var cuentaCapService: CuentaCapService
    private lateinit var cuentaCapRepository: CuentaCapRepository

    // Test dispatcher
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher) // Set test dispatcher
        cuentaCapRepository = CuentaCapRepository(cuentaCapService)

        // Mock Logs
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the dispatcher after tests
    }

    // 1. Test for a successful API response
    @Test
    fun `getCuentaCap returns success when API response is successful`() = runTest {
        // Arrange
        val mockResponse = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro")
        val response = Response.success(mockResponse)

        Mockito.`when`(cuentaCapService.getCuentaCap(token.value, username.value))
            .thenReturn(response)

        // Act
        val result = cuentaCapRepository.getCuentaCap(token.value, username.value)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(mockResponse, (result as Result.Success).data)
    }

    // 2. Test when API returns null body
    @Test
    fun `getCuentaCap returns error when API response body is null`() = runTest {
        // Arrange
        val response = Response.success<CuentaCapResponse>(null)

        Mockito.`when`(cuentaCapService.getCuentaCap(token.value, username.value))
            .thenReturn(response)

        // Act
        val result = cuentaCapRepository.getCuentaCap(token.value, username.value)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("Respuesta vacía", (result as Result.Error).exception.message)
    }

    // 3. Test for API failure (HTTP error response)
    @Test
    fun `getCuentaCap returns error when API call fails with HTTP error`() = runTest {
        // Arrange
        val response = Response.error<CuentaCapResponse>(400, ResponseBody.create(null, "Bad Request"))

        Mockito.`when`(cuentaCapService.getCuentaCap(token.value, username.value))
            .thenReturn(response)

        // Act
        val result = cuentaCapRepository.getCuentaCap(token.value, username.value)

        // Assert
        //assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception.message!!.contains("400 Bad Request"))
    }

    // 4. Test for exception handling in API call
    @Test
    fun `getCuentaCap returns error when API call throws an exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        Mockito.`when`(cuentaCapService.getCuentaCap(token.value, username.value))
            .thenThrow(exception)

        // Act
        val result = cuentaCapRepository.getCuentaCap(token.value, username.value)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("Network error", (result as Result.Error).exception.message)
    }

    // 5. Test coroutine context (ensuring it runs on IO Dispatcher)
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `getCuentaCap runs in Dispatchers IO`() = runTest {
        // Arrange
        val mockResponse = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro"
        )
        val response = Response.success(mockResponse)

        Mockito.`when`(cuentaCapService.getCuentaCap(token.value, username.value))
            .thenReturn(response)

        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = CuentaCapRepository(cuentaCapService)

        // Act
        withContext(dispatcher) {
            repository.getCuentaCap(token.value, username.value)
        }

        // Assert
        assertEquals(dispatcher, coroutineContext[CoroutineDispatcher.Key])
    }

}

/*
class CuentaCapRepositoryTest {

    @Before
    fun setup() {
        mockkObject(SessionManager)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll() // Clean up mocks after each test
    }

    // Mock the Retrofit service
    private val mockService: CuentaCapService = mockk()
    private val repository = CuentaCapRepository(mockService)

    @Test
    fun `getCuentaCap returns Success on valid response`() = runTest {
        // Arrange
        val mockData = CuentaCapResponse(
            FECHAAPERTURA = "2023-01-01",
            NROCUENTA = 123456789L,
            SALDOCONTABLE = "1000.00",
            TIPOCUENTA = "Ahorro"
        )
        coEvery { mockService.getCuentaCap(any(), any()) } returns Response.success(mockData)

        // Act
        val result = repository.getCuentaCap(token.value, username.value)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(mockData, (result as Result.Success).data)
    }


    @Test
    fun `getCuentaCap returns Error on empty response body`() = runTest {
        // Arrange
        coEvery { mockService.getCuentaCap(any(), any()) } returns Response.success(null)

        // Act
        val result = repository.getCuentaCap( token.value, username.value)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("Respuesta vacía", (result as Result.Error).exception.message)
    }

    @Test
    fun `getCuentaCap returns Error on network exception`() = runTest {
        // Arrange
        val exception = Exception("Network failure")
        coEvery { mockService.getCuentaCap(any(), any()) } throws exception

        // Act
        val result = repository.getCuentaCap(token.value, username.value)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("Network failure", (result as Result.Error).exception.message)
    }
}*/