package com.example.oriencoop_score.repository

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import com.example.oriencoop_score.api.CuentaCapService
import com.example.oriencoop_score.api.LoginService
import com.example.oriencoop_score.model.HiddenLoginResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.model.UserLoginResponse
import org.junit.Assert.assertThrows
import java.io.IOException



class LoginRepositoryTest{
    private lateinit var loginService: LoginService
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setup() {
        // Initialize MockK annotations and create mocks
        MockKAnnotations.init(this)
        loginService = mockk()
        loginRepository = LoginRepository(loginService)
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
    fun `when performHiddenLogin is success returns Success`() = runTest {
        // Arrange
        val expectedResponse = HiddenLoginResponse("Hidden Login Successful", "token123")
        coEvery { loginService.hiddenLogin(any()) } returns Response.success(expectedResponse)

        // Act
        val result = loginRepository.performHiddenLogin("user", "pass")

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, (result as Result.Success).data)
    }

    @Test
    fun `when performHiddenLogin throws ioException returns Error` () = runTest {
        // Arrange
        coEvery { loginService.hiddenLogin(any()) } throws IOException("Network error")

        // Act
        val result = loginRepository.performHiddenLogin("user", "pass")

        // Assert
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is IOException)
    }

    @Test
    fun `when performHiddenLogin is error Response throws Exception` () = runTest {
        // Arrange
        val errorResponse = mockk<Response<HiddenLoginResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns null
        }
        coEvery { loginService.hiddenLogin(any()) } returns errorResponse

        // Act
        var exceptionThrown = false // Flag to track if exception was thrown
        try {
            loginRepository.performHiddenLogin("user", "pass")
        } catch (e: Exception) {
            exceptionThrown = true // Exception caught!
            // Optionally, you could assert something about the exception itself here if needed.
        }

        // Assert
        assertTrue("Expected an Exception to be thrown", exceptionThrown)
    }

    @Test
    fun `when performUserLogin is success returns Success` () = runTest {
        // Arrange
        val expectedResponse = UserLoginResponse("message", "rut123")
        coEvery { loginService.userLogin(any(), any()) } returns Response.success(expectedResponse)

        // Act
        val result = loginRepository.performUserLogin("token", "rut", "pass")

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse, (result as Result.Success).data)
    }

    @Test
    fun `when performUserLogin has nullBody returnsError` () = runTest {
        // Arrange
        coEvery { loginService.userLogin(any(), any()) } returns Response.success(null)

        // Act
        val result = loginRepository.performUserLogin("token", "rut", "pass")

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("Response body is null", (result as Result.Error).exception.message)
    }
}

