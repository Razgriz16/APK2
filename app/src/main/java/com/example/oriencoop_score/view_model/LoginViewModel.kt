package com.example.oriencoop_score.view_model


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.Usuario
import com.example.oriencoop_score.repository.LoginRepository
import kotlinx.coroutines.launch
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
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

    // Estados posibles de la UI para el proceso de login.
    sealed class LoginUiState {
        object Idle : LoginUiState()
        object Loading : LoginUiState()
        data class Success(val usuario: Usuario) : LoginUiState()
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

                val result = loginRepository.performUserLogin("6600427", "1234") // parametros originales: cleanRut(rut), password
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
                                    "$functionName: Sesión guardada exitosamente para usuario: ${authData.usuario.nombre}, RUT: ${authData.usuario.rut}"
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
}



//5915933
/*
3224212
28D3A86A1E9401BFD67266D7189936A6638DA54E
5980334
CFC253C1E446785B61AB66ACA3D2A36C332463C2
20069723
DF60F113C0F65E52B746558911CC0059E083BE09
14345673
13793E4CEB511A3F9BD9BC5AEC29851697CF7E3C
21186098 (21.186.098-7)
6117B8AEAA2327F9E05202EF3C8FC0C6F5556353 (Forest28)

6814361
2B08CE9284F126C397B47DEBA547EBE0C2B2FE87

8373229
5EF322F0FE24EA246B1BD8FF3EEAE732225E6886
*/

