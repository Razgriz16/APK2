package com.example.oriencoop_score.repository

import android.media.AudioRecord.OnRecordPositionUpdateListener
import android.util.Log
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.api.CuentaCapService
import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.model.RutRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CuentaCapRepository @Inject constructor(private val cuentaCapService: CuentaCapService) {
    suspend fun getCuentaCap(token: String, rut: String): Result<CuentaCapResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("CuentaCapRepository", "llamando función getCuentaCap")
                val response = cuentaCapService.getCuentaCap(token, rut)
                if (response.isSuccessful) {
                    Log.d("CuentaCapRepository", "llamada api exitosa. BODY: ${response.body()}")
                    response.body()?.let{Result.Success(it)}?: Result.Error(Exception("Respuesta vacía"))
                } else {
                    Log.e("CuentaCapRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("CuentaCapRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}

/*
Pruebas unitarias:

Verificar cuando la llamada a la API es exitosa.
Verificar cuando la llamada a la API falla.
Verificar el manejo de excepciones durante la llamada a la API.
Verfificar el manejo de respuestas vacías.
Verificar cuando que pasa cuando los parámetros son nulos
 */
