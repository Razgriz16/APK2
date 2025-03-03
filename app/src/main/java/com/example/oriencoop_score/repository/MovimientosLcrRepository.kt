package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.MovimientosLcrService
import com.example.oriencoop_score.model.MovimientosLcrResponse
import com.example.oriencoop_score.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientosLcrRepository @Inject constructor(private val movimientosLcrService: MovimientosLcrService) {
    suspend fun getMovimientosLcr(token: String, rut: String): Result<MovimientosLcrResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MovimientosLcrRepository", "llamando función getMovimientosLcr")
                val response = movimientosLcrService.getMovimientosLcr(token, rut)
                if (response.isSuccessful) {
                    Log.d("MovimientosLcrRepository", "Llamada exitosa. BODY "+response.body())
                    Result.Success(response.body()!!)
                }
                else {
                    Log.e("MovimientosLcrRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("MovimientosLcrRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}