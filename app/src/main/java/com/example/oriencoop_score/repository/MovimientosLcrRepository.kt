package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.di.MisMovimientosLcr
import com.example.oriencoop_score.api.MovimientosService
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.MovimientosLcr
import com.example.oriencoop_score.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientosLcrRepository @Inject constructor(
    @MisMovimientosLcr private val movimientosService: MovimientosService
) {

    // Define una etiqueta TAG constante para los logs de esta clase.
    companion object {
        private const val TAG = "MovimientosLcrRepository"
    }

    /**
     * Obtiene los movimientos de línea de crédito para un usuario específico mediante la API.
     * Ejecuta la llamada de red en el hilo de IO y maneja la respuesta específica.
     *
     * @param numeroCuenta Número de cuenta del usuario para filtrar los movimientos.
     * @param cantidad Cantidad de movimientos a obtener (por defecto 20).
     * @param token Token de autorización para la autenticación.
     * @return Un [Result] que contiene [Result.Success] con [ApiResponse<MovimientosLcr>]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    suspend fun getMovimientosLcr(numeroCuenta: Int, cantidad: Int, token: String): Result<ApiResponse<MovimientosLcr>> {
        val functionName = "getMovimientosLcr"
        val tokenBearer = "Bearer $token"
        Log.d(TAG, "$functionName: Iniciando llamada a la API para numeroCuenta: $numeroCuenta, cantidad: $cantidad")
        return withContext(Dispatchers.IO) {
            try {
                val response = movimientosService.getMovimientosLcr(numeroCuenta, cantidad, tokenBearer)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.error_code == 0 && body.data.isNotEmpty()) {
                            Log.d(TAG, "$functionName: Llamada exitosa. ${body.count} movimientos recibidos.")
                            Result.Success(body)
                        } else {
                            Log.e(TAG, "$functionName: Error en la respuesta de la API. Código: ${body.error_code}")
                            Result.Error(IOException("Error de API: Código ${body.error_code}"))
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