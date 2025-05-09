package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.MisProductosService
import com.example.oriencoop_score.di.MisProductosCredito
import com.example.oriencoop_score.di.MisProductosDap
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.Dap
import com.example.oriencoop_score.model.ProductoRepository
import com.example.oriencoop_score.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DapRepository @Inject constructor(
    @MisProductosDap private val misProductosService: MisProductosService
) : ProductoRepository<Dap> {

    // Define una etiqueta TAG constante para los logs de esta clase.
    companion object {
        private const val TAG = "DapRepository"
    }

    /**
     * Obtiene la información de DAP para un usuario mediante la API.
     * Ejecuta la llamada de red en el hilo de IO y maneja la respuesta específica.
     *
     * @param rut La cédula del usuario.
     * @param token El token de autenticación.
     * @return Un [Result] que contiene [Result.Success] con [ApiResponse<Dap>]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    override suspend fun fetchProducto(rut: String, token: String?): Result<ApiResponse<Dap>> {
        val functionName = "getDap"
        val tokenBearer = "Bearer $token"
        Log.d(TAG, "$functionName: Iniciando llamada a la API para cédula: $rut")
        return withContext(Dispatchers.IO) {
            try {
                val response = misProductosService.getDap(rut, tokenBearer)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.error_code == 0) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Datos de DAP recibidos.")
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