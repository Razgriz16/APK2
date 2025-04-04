package com.example.oriencoop_score.view_model.login


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cleanRut
import com.example.oriencoop_score.utility.LoginState
import com.example.oriencoop_score.repository.LoginRepository
import kotlinx.coroutines.launch
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import getClaveSHA1
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import validRut
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    // Estado del formulario (username y password)
    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _rutValid = MutableStateFlow<Boolean?>(null) // Null initially

    // Estado global del login (éxito, error, carga)
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)

    // Exponer estados para UI
    val username: StateFlow<String> = _username
    val password: StateFlow<String> = _password
    val loginState: StateFlow<LoginState> = _loginState
    val rutValid: StateFlow<Boolean?> = _rutValid

    // Token obtenido (público pero solo lectura)
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

    private var isLoggingIn = false


    // Actualizar campos del formulario
    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun performLogin(username: String, password: String) {
        /*
        if (isLoggingIn) {
            Log.d("LoginViewModel", "Login already in progress, skipping call")
            return
        }*/
        Log.d("LoginViewModel", "performLogin called with username: $username")
        isLoggingIn = true
        viewModelScope.launch {
            try {
                // Login oculto
                Log.d("Login", "Realizando Login Entidad (Hidden Login)")
                val hiddenLoginResult =
                    loginRepository.performHiddenLogin("admin", "securepassword")
                if (hiddenLoginResult is Result.Success) {
                    val token = hiddenLoginResult.data.token // Extract the token field
                    Log.d("Login", "Login Entidad Exitoso")
                    _token.value = token // Guardar el token
                    // Step 2: Perform User Login with user-provided credentials and token
                    Log.d("Login", "Realizando Login Cliente (User Login) con token: $token")
                    val rut = cleanRut(username)
                    val _rutValid = validRut(rut)
                    val clave = getClaveSHA1(rut, password)
                    if (_rutValid) {
                        val userLoginResult = loginRepository.performUserLogin(token, "5980334", "CFC253C1E446785B61AB66ACA3D2A36C332463C2")
                        //clave?.let { loginRepository.performUserLogin(token, rut, it) }
                        if (userLoginResult is Result.Success) {
                            Log.d("Login", "Login Cliente Exitoso")
                            _username.value = username
                            _password.value = password // Guardar el nombre de usuario
                            _loginState.value = LoginState.Success(userLoginResult.data)
                            sessionManager.saveSession("5980334", "CFC253C1E446785B61AB66ACA3D2A36C332463C2")
                        } else if (userLoginResult is Result.Error) {
                            _loginState.value = LoginState.Error(
                                userLoginResult.exception.message ?: "User login failed"
                            )
                            Log.e("Login", "Error en el Login Cliente: ${userLoginResult.exception.message}")
                        }
                    } else {
                        Log.d("Login", "Rut incorrecto")
                    }
                } else if (hiddenLoginResult is Result.Error) {
                    _loginState.value = LoginState.Error(
                        hiddenLoginResult.exception.message ?: "Hidden login failed"
                    )
                    Log.e(
                        "Login",
                        "Error en el Login Entidad: ${hiddenLoginResult.exception.message}"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "An unexpected error occurred")
                Log.e("Login", "Error en el Login: ${e.message}")
            }

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

