package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.api.ClienteService
import com.example.oriencoop_score.model.AddressResponse
import com.example.oriencoop_score.model.Mail
import com.example.oriencoop_score.model.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class ClienteRepository @Inject constructor(private val clienteService: ClienteService) {

    // Etiqueta constante para los logs de esta clase
    companion object {
        private const val TAG = "UserRepository"
    }

    /**
     * Obtiene la información de los usuarios desde la API.
     *
     * @return Un [Result] que contiene [Result.Success] con [UserResponse]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun getUserInfo(rut: String): Result<UserResponse> {
        val functionName = "getUserInfo"
        Log.d(TAG, "$functionName: Iniciando llamada a la API para obtener información de usuarios.")
        return withContext(Dispatchers.IO) {
            try {
                val response = clienteService.getClienteInfo(rut)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.errorCode == 0) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Datos de usuarios recibidos.")
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
     * Verifica un correo electrónico mediante la API.
     *
     * @param email El correo electrónico a verificar.
     * @return Un [Result] que contiene [Result.Success] con [Mail]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun verifyEmail(email: String): Result<Mail> {
        val functionName = "verifyEmail"
        Log.d(TAG, "$functionName: Iniciando llamada a la API para verificar correo: $email.")
        return withContext(Dispatchers.IO) {
            try {
                val response = clienteService.getMail(email)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.errorCode == 0) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Correo verificado.")
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
     * Obtiene la lista de direcciones desde la API.
     *
     * @return Un [Result] que contiene [Result.Success] con [AddressResponse]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun getAddresses(rut: String): Result<AddressResponse> {
        val functionName = "getAddresses"
        Log.d(TAG, "$functionName: Iniciando llamada a la API para obtener direcciones.")
        return withContext(Dispatchers.IO) {
            try {
                val response = clienteService.getAddress(rut)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.errorCode == 0) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Direcciones recibidas.")
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
}