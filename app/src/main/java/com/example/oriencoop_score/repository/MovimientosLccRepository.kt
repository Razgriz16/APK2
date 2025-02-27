package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.api.MovimientosLccService
import com.example.oriencoop_score.model.MovimientosLccResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientosLccRepository @Inject constructor(private val movimientosLccService: MovimientosLccService) {
    suspend fun getMovimientosLcc(token: String, rut: String): Result<MovimientosLccResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MovimientoLccRepository", "llamando función getMovimientosAhorro")
                val response = movimientosLccService.getMovimientosLcc(token, rut)
                if (response.isSuccessful) {
                    Log.d("MovimientoLccRepository", "Llamada exitosa. BODY "+response.body())
                    Result.Success(response.body()!!)
                }
                else {
                    Log.e("MovimientoLccRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("MovimientoLccRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}