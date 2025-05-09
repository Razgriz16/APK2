package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.LoginService
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.AuthResponse
import com.example.oriencoop_score.model.UserLoginRequest
import com.example.oriencoop_score.model.UserLogoutRequest
import com.example.oriencoop_score.model.UserLogoutResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.oriencoop_score.utility.Result
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class LoginRepository @Inject constructor(private val loginService: LoginService) {

    // Define una etiqueta TAG constante para los logs de esta clase.
    companion object {
        private const val TAG = "LoginRepository"
    }

    /**
     * Realiza el login del usuario mediante la API.
     * Ejecuta la llamada de red en el hilo de IO y maneja la respuesta específica.
     *
     * @param rut El RUT del usuario.
     * @param clave La clave del usuario.
     * @return Un [Result] que contiene [Result.Success] con [AuthResponse]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun performUserLogin(rut: String, clave: String): Result<AuthResponse> {
        val functionName = "performUserLogin"
        Log.d(TAG, "$functionName: Iniciando llamada a la API.")
        return withContext(Dispatchers.IO) {
            try {
                val response = loginService.userLogin(UserLoginRequest(rut, clave))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.errorCode == 0 && body.data.isNotEmpty()) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Access token recibido.")
                            Result.Success(body)
                        } else {
                            Log.e(TAG, "$functionName: Error en la respuesta de la API. Código: ${body.errorCode}, Mensaje: ${body.errorMessage}")
                            Result.Error(IOException("Error de API: ${body.errorMessage}"))
                        }
                    } else {
                        Log.e(TAG, "$functionName: Respuesta HTTP exitosa pero cuerpo nulo.")
                        Result.Error(IOException("Respuesta HTTP exitosa pero cuerpo nulo."))
                    }
                } else {
                    Log.e(TAG, "$functionName: Error HTTP. Code: ${response.code()}, Message: ${response.message()}")
                    Result.Error(IOException("Error HTTP: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "$functionName: Excepción durante la llamada a la API.", e)
                Result.Error(e)
            }
        }
    }

    /**
     * Realiza el logout del usuario mediante la API.
     * Ejecuta la llamada de red en el hilo de IO y maneja la respuesta específica.
     *
     * @param token El refresh token del usuario.
     * @return Un [Result] que contiene [Result.Success] con [UserLogoutResponse]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun performUserLogout(token: String): Result<ApiResponse<UserLogoutResponse>> {
        val functionName = "performUserLogout"
        Log.d(TAG, "$functionName: Iniciando llamada a la API.")
        return withContext(Dispatchers.IO) {
            try {
                val response = loginService.userLogout(UserLogoutRequest(token))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.error_code == 0 && body.data.isNotEmpty()) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Access token recibido.")
                            Result.Success(body)
                        } else {
                            Log.e(TAG, "$functionName: Error en la respuesta de la API. Código: ${body.error_code}")
                            Result.Error(IOException("Error de API: ${body.error_code}"))
                        }
                    } else {
                        Log.e(TAG, "$functionName: Respuesta HTTP exitosa pero cuerpo nulo.")
                        Result.Error(IOException("Respuesta HTTP exitosa pero cuerpo nulo."))
                    }
                } else {
                    Log.e(TAG, "$functionName: Error HTTP. Code: ${response.code()}, Message: ${response.message()}")
                    Result.Error(IOException("Error HTTP: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "$functionName: Excepción durante la llamada a la API.", e)
                Result.Error(e)
            }
        }
    }
}