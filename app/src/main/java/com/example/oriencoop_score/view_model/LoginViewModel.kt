package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.Usuario
import com.example.oriencoop_score.repository.LoginRepository
import kotlinx.coroutines.launch
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.UserLogoutResponse
import com.example.oriencoop_score.view_model.LoginViewModel.LoginUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.oriencoop_score.utility.cleanRut
import com.example.oriencoop_score.utility.validRut

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Define una etiqueta TAG constante para los logs de esta clase.
    companion object {
        private const val TAG = "LoginViewModel"
    }

    // Estados posibles de la UI para el proceso de login y logout.
    sealed class LoginUiState {
        object Idle : LoginUiState()
        object Loading : LoginUiState()
        data class Success(val usuario: Usuario) : LoginUiState()
        data class SuccessLogout(val userLogoutResponse: UserLogoutResponse) : LoginUiState()
        data class Error(val message: String) : LoginUiState()
    }

    private val _uiState = MutableStateFlow<LoginUiState>(Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Inicia el proceso de login autenticando al usuario con el RUT y contraseña proporcionados.
     * Actualiza el estado de la UI ([LoginUiState]) según el resultado de la operación.
     * Guarda los datos de la sesión en [SessionManager] si la autenticación es exitosa.
     *
     * @param rut El RUT del usuario.
     * @param password La contraseña del usuario.
     */
    fun login(rut: String, password: String) {
        val functionName = "login"
        Log.d(TAG, "$functionName: Iniciando proceso de login para RUT: $rut")
        viewModelScope.launch {
            _uiState.value = Loading
            Log.d(TAG, "$functionName: Estado cambiado a Loading, verficiación rut")
            if (validRut(rut) == false) {
                Log.d(TAG, "$functionName: Rut incorrecto")
                _uiState.value = Error("Rut incorrecto")
            }
            else {
                Log.d(TAG, "$functionName: Rut correcto")

                val result = loginRepository.performUserLogin(cleanRut(rut), password) // parametros originales: cleanRut(rut), password
                _uiState.value = when (result) {
                    is Result.Success -> {
                        val authResponse = result.data
                        Log.d(
                            TAG,"$functionName: Respuesta de login recibida. ErrorCode: ${authResponse.errorCode}, Data size: ${authResponse.data.size}"
                        )
                        val usuario2 = Usuario("test", cleanRut(rut).toInt())
                        if (authResponse.errorCode == 0 && authResponse.data.isNotEmpty()) {
                            val authData = authResponse.data.first()
                            try {
                                sessionManager.saveSession(
                                    authData.accessToken,
                                    authData.refreshToken,
                                    usuario2

                                )
                                Log.d(
                                    TAG,
                                    "$functionName: Sesión guardada exitosamente para usuario: ${authData.usuario.nombre}, RUT: ${authData.usuario.rut} con token acceso: ${authData.accessToken} y refresh token: ${authData.refreshToken}"
                                )
                                Success(authData.usuario)
                            } catch (e: Exception) {
                                Log.e(TAG, "$functionName: Error al guardar la sesión.", e)
                                Error("Error al guardar la sesión: ${e.message}")
                            }
                        } else {
                            Log.e(
                                TAG,
                                "$functionName: Error en la respuesta de la API. Código: ${authResponse.errorCode}, Mensaje: ${authResponse.errorMessage}"
                            )
                            Error(authResponse.errorMessage)
                        }
                    }

                    is Result.Error -> {
                        Log.e(
                            TAG,
                            "$functionName: Error en el login. Mensaje: ${result.exception.message}"
                        )
                        Error(result.exception.message ?: "Error desconocido")
                    }

                    is Result.Loading -> {
                        Log.w(TAG, "$functionName: Estado Loading no debería ocurrir aquí.")
                        Loading // Esto no debería suceder, pero se maneja por completitud
                    }
                }
            }
            Log.d(TAG, "$functionName: Estado final de la UI: ${_uiState.value}")
        }
    }

    /**
     * Inicia el proceso de logout, llamando a la API para invalidar el refresh token
     * y limpia la sesión local si la operación es exitosa.
     * Actualiza el estado de la UI ([LoginUiState]) según el resultado de la operación.
     */
    fun logout() {
        val functionName = "logout"
        Log.d(TAG, "$functionName: Iniciando proceso de logout")
        viewModelScope.launch {
            _uiState.value = Loading
            Log.d(TAG, "$functionName: Estado cambiado a Loading")
            val refreshToken = sessionManager.getRefreshToken()
            if (refreshToken == null || !sessionManager.isLoggedIn()) {
                Log.w(TAG, "$functionName: No se encontró refresh token o sesión no iniciada, limpiando sesión local")
                sessionManager.clearSession()
                _uiState.value = SuccessLogout(UserLogoutResponse("Sesión Cerrada Localmente"))
            } else {
                val result = loginRepository.performUserLogout(refreshToken)
                _uiState.value = when (result) {
                    is Result.Success -> {
                        val apiResponse = result.data
                        Log.d(
                            TAG,
                            "$functionName: Respuesta de logout recibida. ErrorCode: ${apiResponse.error_code}, Data size: ${apiResponse.data.size}, Count: ${apiResponse.count}"
                        )
                        if (apiResponse.error_code == 0 && apiResponse.data.isNotEmpty()) {
                            sessionManager.clearSession()
                            Log.d(TAG, "$functionName: Sesión limpiada exitosamente tras logout")
                            SuccessLogout(apiResponse.data.first())
                        } else {
                            Log.e(
                                TAG,
                                "$functionName: Error en la respuesta de la API. Código: ${apiResponse.error_code}"
                            )
                            // Clear session to ensure local session is invalidated
                            sessionManager.clearSession()
                            Error("Error de API: Código ${apiResponse.error_code}")
                        }
                    }
                    is Result.Error -> {
                        Log.e(
                            TAG,
                            "$functionName: Error en el logout. Mensaje: ${result.exception.message}"
                        )
                        // Clear session on error to ensure local session is invalidated
                        sessionManager.clearSession()
                        Error(result.exception.message ?: "Error desconocido")
                    }

                    Result.Loading -> TODO()
                }
            }
            Log.d(TAG, "$functionName: Estado final de la UI: ${_uiState.value}")
        }
    }
}

// 6600427-9 (OG&TESTING [1234]: B7EA670FE7067481CE7951F5D86B6A8E155BFEFB)
// 8373229-6 (OG: 5EF322F0FE24EA246B1BD8FF3EEAE732225E6886) (TESTING [1234]: 5DCF48057D8E49383A186F03321C1EB3729C6C37)
// 5980334-4 (OG: CFC253C1E446785B61AB66ACA3D2A36C332463C2) (TESTING [1234]: 8521C1B6B7EF5740EC296B1479AB441ED79AFEA7)
// 3224212-K (OG: 28D3A86A1E9401BFD67266D7189936A6638DA54E) (TESTING [1234]: D83E892162A2828EE27D5F3A0A2EC4A80A8724F0)
// 7791604-0 (OG: 52EF2D705C00F73D7D496FAD5C52FE97BB6E3438) (TESTING [1234]: C365B77959B4B06C2C4140F39920A2285A5AF2D9)
// 5915933