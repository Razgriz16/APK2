package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.api.CuentaAhorroService
import com.example.oriencoop_score.model.CuentaAhorroResponse
import com.example.oriencoop_score.model.MovimientosResponse
import com.example.oriencoop_score.model.RutRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CuentaAhorroRepository @Inject constructor(private val ahorrosService: CuentaAhorroService){
    suspend fun getAhorro(token: String, rut: String): Result<CuentaAhorroResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("CuentaAhorroRepository", "llamando función getAhorro")
                val response = ahorrosService.getAhorro(token, rut)
                if (response.isSuccessful) {
                    Log.d("CuentaAhorroRepository", "Llamada exitosa. BODY " + response.body())
                    Result.Success(response.body()!!)
                } else {
                    Log.e(
                        "CuentaAhorroRepository",
                        "Llamada fallida. Error: ${response.code()} ${response.message()}"
                    )
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("CuentaAhorroRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}