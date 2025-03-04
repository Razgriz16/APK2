package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.Lcc
import com.example.oriencoop_score.model.LccResponse
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
class LccRepositoryTest {

    // Dependencies to be mocked and the class under test
    private lateinit var lccService: LccService

    @Before
    fun setup() {
        // Initialize MockK annotations and create mocks
        MockKAnnotations.init(this)
        lccService = mockk()

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
    fun `getLcc returns success when service responds with data`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val expectedResponse = LccResponse(
            lcc = listOf(
                Lcc(
                    CUPOAUTORIZADO = "300000.00",
                    CUPOUTILIZADO = "120000.50",
                    CUPODISPONIBLE = "179999.50",
                    NROCUENTA = 4567891230L
                )
            )
        )

        coEvery{lccService.getLcc(token, rut)} returns Response.success(expectedResponse)

        // Act
        val repository = LccRepository(lccService)
        val result = repository.getLcc(token, rut)

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, (result as Result.Success).data)
        coVerify { lccService.getLcc(token, rut) }
    }

    @Test
    fun `getLcc returns error when service responds with successful but null body`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<LccResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null
        coEvery { lccService.getLcc(token, rut) } returns mockResponse

        // Act
        val repository = LccRepository(lccService)
        val result = repository.getLcc(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(null, (result as Result.Error).exception.message)
        coVerify { lccService.getLcc(token, rut) }
    }

    @Test
    fun `getLcc returns error when service responds with unsuccessful response`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val mockResponse = mockk<Response<LccResponse>>()
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 404
        every { mockResponse.message() } returns "Not Found"
        coEvery { lccService.getLcc(token, rut) } returns mockResponse

        // Act
        val repository = LccRepository(lccService)
        val result = repository.getLcc(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        val errorMessage = (result as Result.Error).exception.message
        assertTrue(errorMessage?.contains("404") == true)
        assertTrue(errorMessage?.contains("Not Found") == true)
        coVerify { lccService.getLcc(token, rut) }
    }

    @Test
    fun `getLcc returns error when service throws exception`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        val exception = IOException("Network failure")
        coEvery { lccService.getLcc(token, rut) } throws exception

        // Act
        val repository = LccRepository(lccService)
        val result = repository.getLcc(token, rut)

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify { lccService.getLcc(token, rut) }
    }

    @Test
    fun `getLcc calls service with correct parameters`() = runTest {
        // Arrange
        val token = SessionManager().token.value
        val rut = SessionManager().username.value
        coEvery { lccService.getLcc(token, rut) } returns mockk()

        // Act
        val repository = LccRepository(lccService)
        repository.getLcc(token, rut)

        // Assert
        coVerify { lccService.getLcc(token, rut) }
    }
}
