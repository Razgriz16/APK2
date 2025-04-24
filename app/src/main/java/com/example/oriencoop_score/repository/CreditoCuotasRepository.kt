package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.MisProductosCredito
import com.example.oriencoop_score.api.MisProductosService
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.CreditoCuotas
import com.example.oriencoop_score.utility.ProductoRepository
import com.example.oriencoop_score.utility.Result
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditoCuotasRepository @Inject constructor(
    @MisProductosCredito private val creditoCuotasService: MisProductosService
) : ProductoRepository<CreditoCuotas> {

    // Define una etiqueta TAG constante para los logs de esta clase.
    companion object {
        private const val TAG = "CreditoCuotasRepository"
    }

    /**
     * Obtiene la lista de cuotas de crédito para un usuario mediante la API.
     * Ejecuta la llamada de red en el hilo de IO y maneja la respuesta específica.
     *
     * @param rut La cédula del usuario.
     * @return Un [Result] que contiene [Result.Success] con [CreditoCuotas]
     *         o [Result.Error] con la [Throwable] correspondiente.
     */
    override suspend fun fetchProducto(rut: String): Result<ApiResponse<CreditoCuotas>> {
        val functionName = "getCreditoCuotas"
        Log.d(TAG, "$functionName: Iniciando llamada a la API para cédula: $rut")
        return withContext(Dispatchers.IO) {
            try {
                val response = creditoCuotasService.getCreditoCuotas(rut)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.error_code == 0 && body.data.isNotEmpty()) {
                            Log.d(TAG, "$functionName: Llamada exitosa. Datos de cuotas recibidos.")
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


/*
@Singleton
class CreditoCuotasRepository @Inject constructor(private val creditoCuotasService: MisProductosService){
    suspend fun getCreditoCuotas(token: String, rut: String): Result<CreditoCuotasResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("CreditoCuotasRepository", "llamando función getCreditoCuotas")
                val response = creditoCuotasService.getCreditoCuotas(token, rut)
                if (response.isSuccessful) {
                    Log.d("CreditoCuotasRepository", "Llamada exitosa. BODY " + response.body())
                    Result.Success(response.body()!!)
                } else {
                    Log.e(
                        "CreditoCuotasRepository",
                        "Llamada fallida. Error: ${response.code()} ${response.message()}"
                    )
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("CreditoCuotasRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}
 */