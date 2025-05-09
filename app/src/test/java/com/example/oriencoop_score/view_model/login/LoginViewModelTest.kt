package com.example.oriencoop_score.view_model.login

import android.util.Log
import com.example.oriencoop_score.utility.LoginState
import com.example.oriencoop_score.model.HiddenLoginResponse
import com.example.oriencoop_score.model.UserLoginResponse
import com.example.oriencoop_score.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
/*
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var mockRepository: LoginRepository
    private lateinit var sessionManager: SessionManager
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk() // Mock the repository directly
        sessionManager = mockk() // Mock the SessionManager directly
        every { sessionManager.token } returns MutableStateFlow("tokenValue")
        every { sessionManager.username } returns MutableStateFlow("usernameValue")
        viewModel = LoginViewModel(mockRepository, sessionManager)
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

    @Test
    fun `updateUsername should update username state`() {
        // Arrange: (ViewModel already initialized in setUp)

        // Act
        viewModel.updateUsername("new_username")

        // Assert
        assertEquals("new_username", viewModel.username.value)
    }

    @Test
    fun `updatePassword should update password state`() {
        // Arrange: (ViewModel already initialized)

        // Act
        viewModel.updatePassword("new_password")

        // Assert
        assertEquals("new_password", viewModel.password.value)
    }

    @Test
    fun `performLogin should succeed when both logins are successful`() = runTest {
        // Arrange
        val hiddenLoginResponse = HiddenLoginResponse("Hidden Login Successful", "token123")
        val userLoginResponse = UserLoginResponse("User Login Successful", "12345678-9")
        coEvery { mockRepository.performHiddenLogin("admin", "securepassword") } returns Result.Success(hiddenLoginResponse)
        coEvery { mockRepository.performUserLogin("token123", "5980334", "CFC253C1E446785B61AB66ACA3D2A36C332463C2") } returns Result.Success(userLoginResponse)

        // Act
        viewModel.performLogin("admin", "securepassword")
        // Advance the test dispatcher to execute the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(LoginState.Success(userLoginResponse), viewModel.loginState.value)
        assertEquals("token123", viewModel.token.value)
    }

    @Test
    fun `performLogin should fail when hidden login fails`() = runTest {
        // Arrange
        coEvery { mockRepository.performHiddenLogin("admin", "securepassword") } returns Result.Error(Exception("Hidden login failed"))

        // Act
        viewModel.performLogin("username", "password")
        // Advance the test dispatcher to execute the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.loginState.value as LoginState.Error
        assertEquals("Hidden login failed", state.message)
        coVerify(exactly = 0) { mockRepository.performUserLogin(any(), any(), any()) } // Ensure user login isn’t called
    }

    @Test
    fun `performLogin should fail when user login fails`() = runTest {
        // Arrange
        val hiddenLoginResponse = HiddenLoginResponse("Hidden Login Successful", "token123")
        coEvery { mockRepository.performHiddenLogin("admin", "securepassword") } returns Result.Success(hiddenLoginResponse)
        coEvery { mockRepository.performUserLogin("token123", "5980334", "CFC253C1E446785B61AB66ACA3D2A36C332463C2") } returns Result.Error(Exception("User login failed"))

        // Act
        viewModel.performLogin("username", "password")
        // Advance the test dispatcher to execute the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.loginState.value as LoginState.Error
        assertEquals("User login failed", state.message)
        assertEquals("token123", viewModel.token.value) // Token should still be set
    }


}



/*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private var loginService: LoginService
    private val loginRepository: LoginRepository(loginService)
    private lateinit var viewModel: LoginViewModel(loginRepository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginRepository)
        // Mock Logs
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `performLogin successful Hidden And UserLogin updates State And Token`() = runTest {
        // Mock responses
        val hiddenToken = "hidden_token_123"
        val userResponse = UserLoginResponse("User Login Successful", "12345678-9")
        val hiddenLoginResponse = HiddenLoginResponse("Hidden Login Successful", hiddenToken)

        coEvery { loginRepository.performHiddenLogin("admin", "securepassword") }returns Result.Success(
            hiddenLoginResponse
        )
        coEvery { loginRepository.performUserLogin(hiddenToken, "5980334", "CFC253C1E446785B61AB66ACA3D2A36C332463C2") } returns Result.Success(
            userResponse)

        // Trigger login
        viewModel.performLogin("admin", "securepassword")


        // Verify token and state
        viewModel.token.test {
            assertEquals(hiddenToken, awaitItem())
            cancel()
        }

        viewModel.loginState.test {
            assertEquals(LoginState.Idle, awaitItem())
            assertTrue(awaitItem() is LoginState.Success)
            cancel()
        }

        // Verify repository calls
        coVerify { loginRepository.performHiddenLogin("admin", "securepassword") }
        coVerify { loginRepository.performUserLogin(hiddenToken, "5980334", "CFC253C1E446785B61AB66ACA3D2A36C332463C2") }
    }
}*/