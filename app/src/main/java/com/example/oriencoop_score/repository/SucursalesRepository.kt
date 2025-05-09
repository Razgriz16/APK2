package com.example.oriencoop_score.repository


import android.util.Log
import com.example.oriencoop_score.api.SucursalesService
import com.example.oriencoop_score.model.Ciudades
import com.example.oriencoop_score.model.Comunas
import com.example.oriencoop_score.model.Sucursales
import com.example.oriencoop_score.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para manejar la obtención de datos relacionados con sucursales, ciudades y comunas
 * desde la API. Abstrae las fuentes de datos de red.
 *
 * @property sucursalesService Instancia del servicio Retrofit inyectada.
 */
@Singleton
class SucursalesRepository @Inject constructor(private val sucursalesService: SucursalesService) {

    // Define una etiqueta TAG constante para los logs de esta clase.
    companion object {
        private const val TAG = "SucursalesRepository"
    }

    /**
     * Obtiene la lista de ciudades desde la API.
     * Realiza la llamada de red en el hilo de IO y maneja la respuesta específica.
     *
     * @return Un [Result] que contiene [Result.Success] con [List<Ciudades>]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun getCiudades(token: String): Result<List<Ciudades>> {
        val functionName = "getCiudades"
        val tokenBearer= "Bearer $token"
        Log.d(TAG, "$functionName: Iniciando llamada a la API.")
        // Ejecuta el bloque en el contexto de Dispatchers.IO.
        return withContext(Dispatchers.IO) {
            try {
                // Realiza la llamada a la API.
                val response = sucursalesService.getCiudades(tokenBearer)

                // Verifica si la llamada HTTP fue exitosa (código 2xx).
                if (response.isSuccessful) {
                    val body = response.body()
                    // Verifica si el cuerpo de la respuesta no es nulo.
                    if (body != null) {
                        // Verifica el código de error específico de la API.
                        // Asumimos 0.0f como éxito según la estructura de CiudadesResponse.
                        if (body.error_code == 0.0f) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Datos recibidos: ${body.data.size} ciudades.")
                            // Éxito: Retorna los datos, o una lista vacía si 'data' es null.
                            Result.Success(body.data)
                        } else {
                            // Error lógico reportado por la API.
                            val errorMessage = "$functionName: Error lógico de API. Code: ${body.error_code}, Message: ${body.error_message}"
                            Log.e(TAG, errorMessage)
                            Result.Error(IOException(errorMessage)) // Encapsula como IOException
                        }
                    } else {
                        // Cuerpo nulo inesperado en respuesta HTTP exitosa.
                        val errorMessage = "$functionName: Respuesta HTTP exitosa pero cuerpo nulo."
                        Log.e(TAG, errorMessage)
                        Result.Error(IOException(errorMessage))
                    }
                } else {
                    // Error HTTP (ej: 404, 500).
                    val errorMessage = "$functionName: Error HTTP. Code: ${response.code()}, Message: ${response.message()}"
                    Log.e(TAG, errorMessage)
                    Result.Error(IOException(errorMessage))
                }
            } catch (e: Exception) {
                // Captura cualquier otra excepción (red, parseo JSON, etc.).
                Log.e(TAG, "$functionName: Excepción durante la llamada a la API.", e)
                Result.Error(e)
            }
        }
    }

    /**
     * Obtiene la lista de comunas desde la API.
     * Realiza la llamada de red en el hilo de IO y maneja la respuesta específica.
     *
     * @return Un [Result] que contiene [Result.Success] con [List<Comunas>]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun getComunas(token: String): Result<List<Comunas>> {
        val functionName = "getComunas"
        val tokenBearer= "Bearer $token"
        Log.d(TAG, "$functionName: Iniciando llamada a la API.")
        return withContext(Dispatchers.IO) {
            try {
                val response = sucursalesService.getComunas(tokenBearer)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Asumimos 0.0f como éxito según la estructura de ComunasResponse.
                        if (body.error_code == 0.0f) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Datos recibidos: ${body.data.size} comunas.")
                            Result.Success(body.data)
                        } else {
                            val errorMessage = "$functionName: Error lógico de API. Code: ${body.error_code}, Message: ${body.error_message}"
                            Log.e(TAG, errorMessage)
                            Result.Error(IOException(errorMessage))
                        }
                    } else {
                        val errorMessage = "$functionName: Respuesta HTTP exitosa pero cuerpo nulo."
                        Log.e(TAG, errorMessage)
                        Result.Error(IOException(errorMessage))
                    }
                } else {
                    val errorMessage = "$functionName: Error HTTP. Code: ${response.code()}, Message: ${response.message()}"
                    Log.e(TAG, errorMessage)
                    Result.Error(IOException(errorMessage))
                }
            } catch (e: Exception) {
                Log.e(TAG, "$functionName: Excepción durante la llamada a la API.", e)
                Result.Error(e)
            }
        }
    }

    /**
     * Obtiene la lista de sucursales desde la API.
     * Realiza la llamada de red en el hilo de IO y maneja la respuesta específica.
     * Notar que SucursalesResponse *no* tiene error_code/error_message.
     *
     * @return Un [Result] que contiene [Result.Success] con [List<Sucursales>]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun getSucursales(token: String): Result<List<Sucursales>> {
        val functionName = "getSucursales"
        val tokenBearer= "Bearer $token"
        Log.d(TAG, "$functionName: Iniciando llamada a la API.")
        return withContext(Dispatchers.IO) {
            try {
                val response = sucursalesService.getSucursales(tokenBearer)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // SucursalesResponse no tiene error_code/message, asumimos éxito si HTTP es 2xx y body no es null.
                        Log.d(TAG, "$functionName: Llamada exitosa. Datos recibidos: ${body.data.size} sucursales.")
                        Result.Success(body.data)
                    } else {
                        val errorMessage = "$functionName: Respuesta HTTP exitosa pero cuerpo nulo."
                        Log.e(TAG, errorMessage)
                        Result.Error(IOException(errorMessage))
                    }
                } else {
                    val errorMessage = "$functionName: Error HTTP. Code: ${response.code()}, Message: ${response.message()}"
                    Log.e(TAG, errorMessage)
                    Result.Error(IOException(errorMessage))
                }
            } catch (e: Exception) {
                Log.e(TAG, "$functionName: Excepción durante la llamada a la API.", e)
                Result.Error(e)
            }
        }
    }
}