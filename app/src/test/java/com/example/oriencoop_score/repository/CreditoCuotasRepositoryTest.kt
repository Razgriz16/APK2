package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.api.CreditoCuotasService
import com.example.oriencoop_score.model.CreditoCuota
import com.example.oriencoop_score.model.CreditoCuotasResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class CreditoCuotasRepositoryTest {

    // Dependencies to be mocked and the class under test
    private lateinit var creditoCuotasService: CreditoCuotasService

    @Before
    fun setup() {
        // Initialize MockK annotations and create mocks
        MockKAnnotations.init(this)
        creditoCuotasService = mockk()

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
    fun `getCreditoCuota returns success when service responds with data`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val expectedResponse = CreditoCuotasResponse(
            credito_cuotas = listOf(
                CreditoCuota(
                    MONTOCREDITO = "200000.00",
                    NROCUENTA = 1234567890L,
                    NUMEROCUOTAS = 24,
                    PROXVENCIMIENTO = "2025-03-20",
                    TIPOCUENTA = "Crédito Hipotecario",
                    VALORCUOTA = "9500.25"
                ),
                CreditoCuota(
                    MONTOCREDITO = "75000.00",
                    NROCUENTA = 9876543210L,
                    NUMEROCUOTAS = 12,
                    PROXVENCIMIENTO = "2025-04-10",
                    TIPOCUENTA = "Crédito de Vehículo",
                    VALORCUOTA = "6500.80"
                )
            )
        )
        coEvery { creditoCuotasService.getCreditoCuotas(token, rut) } returns Response.success(expectedResponse)

        // Act
        val repository = CreditoCuotasRepository(creditoCuotasService)
        val result = repository.getCreditoCuotas(token, rut)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, (result as Result.Success).data)
        coVerify { creditoCuotasService.getCreditoCuotas(token, rut) }
    }

    @Test
    fun `getCreditoCuotas returns error when service responds with successful but null body`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<CreditoCuotasResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null
        coEvery { creditoCuotasService.getCreditoCuotas(token, rut) } returns mockResponse

        // Act
        val repository = CreditoCuotasRepository(creditoCuotasService)
        val result = repository.getCreditoCuotas(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(null, (result as Result.Error).exception.message)
        coVerify { creditoCuotasService.getCreditoCuotas(token, rut) }
    }

    @Test
    fun `getCreditoCuotas returns error when service responds with unsuccessful response`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<CreditoCuotasResponse>>()
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 404
        every { mockResponse.message() } returns "Not Found"
        coEvery { creditoCuotasService.getCreditoCuotas(token, rut) } returns mockResponse

        // Act
        val repository = CreditoCuotasRepository(creditoCuotasService)
        val result = repository.getCreditoCuotas(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        val errorMessage = (result as Result.Error).exception.message
        assertTrue(errorMessage?.contains("404") == true)
        assertTrue(errorMessage?.contains("Not Found") == true)
        coVerify { creditoCuotasService.getCreditoCuotas(token, rut) }
    }

    @Test
    fun `getCreditoCuotas returns error when service throws exception`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val exception = IOException("Network failure")
        coEvery { creditoCuotasService.getCreditoCuotas(token, rut) } throws exception

        // Act
        val repository = CreditoCuotasRepository(creditoCuotasService)
        val result = repository.getCreditoCuotas(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify { creditoCuotasService.getCreditoCuotas(token, rut) }
    }

    @Test
    fun `getCreditoCuotas calls service with correct parameters`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        coEvery { creditoCuotasService.getCreditoCuotas(token, rut) } returns mockk()

        // Act
        val repository = CreditoCuotasRepository(creditoCuotasService)
        repository.getCreditoCuotas(token, rut)

        // Assert
        coVerify { creditoCuotasService.getCreditoCuotas(token, rut) }
    }
}
