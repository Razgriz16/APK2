package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.model.CuentaAhorroResponse
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
class CuentaAhorroRepositoryTest {

    // Dependencies to be mocked and the class under test
    private lateinit var cuentaAhorroService: CuentaAhorroService

    @Before
    fun setup() {
        // Initialize MockK annotations and create mocks
        MockKAnnotations.init(this)
        cuentaAhorroService = mockk()

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
    fun `getAhorro returns success when service responds with data`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val expectedResponse = CuentaAhorroResponse(
            ahorro = listOf(
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Infantil",
                    SALDODISPONIBLE = "15000.30",
                    SALDOCONTABLE = "15500.00",
                    SUCURSAL = "Sucursal Este",
                    FECHAAPERTURA = "2024-01-10",
                    NROCUENTA = 4567891230L
                ),
                CuentaAhorro(
                    TIPOCUENTA = "Ahorro Empresarial",
                    SALDODISPONIBLE = "300000.00",
                    SALDOCONTABLE = "310000.75",
                    SUCURSAL = "Sucursal Sur",
                    FECHAAPERTURA = "2021-09-25",
                    NROCUENTA = 3210987654L
                )
            )
        )
        coEvery { cuentaAhorroService.getAhorro(token, rut) } returns Response.success(expectedResponse)

        // Act
        val repository = CuentaAhorroRepository(cuentaAhorroService)
        val result = repository.getAhorro(token, rut)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, (result as Result.Success).data)
        coVerify { cuentaAhorroService.getAhorro(token, rut) }
    }

    @Test
    fun `getAhorro returns error when service responds with successful but null body`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<CuentaAhorroResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null
        coEvery { cuentaAhorroService.getAhorro(token, rut) } returns mockResponse

        // Act
        val repository = CuentaAhorroRepository(cuentaAhorroService)
        val result = repository.getAhorro(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("Respuesta vacía", (result as Result.Error).exception.message)
        coVerify { cuentaAhorroService.getAhorro(token, rut) }
    }

    @Test
    fun `getAhorro returns error when service responds with unsuccessful response`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<CuentaAhorroResponse>>()
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 404
        every { mockResponse.message() } returns "Not Found"
        coEvery { cuentaAhorroService.getAhorro(token, rut) } returns mockResponse

        // Act
        val repository = CuentaAhorroRepository(cuentaAhorroService)
        val result = repository.getAhorro(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        val errorMessage = (result as Result.Error).exception.message
        assertTrue(errorMessage?.contains("404") == true)
        assertTrue(errorMessage?.contains("Not Found") == true)
        coVerify { cuentaAhorroService.getAhorro(token, rut) }
    }

    @Test
    fun `getAhorro returns error when service throws exception`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val exception = IOException("Network failure")
        coEvery { cuentaAhorroService.getAhorro(token, rut) } throws exception

        // Act
        val repository = CuentaAhorroRepository(cuentaAhorroService)
        val result = repository.getAhorro(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify { cuentaAhorroService.getAhorro(token, rut) }
    }

    @Test
    fun `getAhorro calls service with correct parameters`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        coEvery { cuentaAhorroService.getAhorro(token, rut) } returns mockk()

        // Act
        val repository = CuentaAhorroRepository(cuentaAhorroService)
        repository.getAhorro(token, rut)

        // Assert
        coVerify { cuentaAhorroService.getAhorro(token, rut) }
    }
}
