package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.api.MovimientosService
import com.example.oriencoop_score.model.MovimientosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientosRepository @Inject constructor(private val movimientosService: MovimientosService) {


    suspend fun getMovimientos(token: String, rut: String): Result<MovimientosResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MovimientosRepository", "llamando función getMovimientos")
                val response = movimientosService.getMovimientos(token, rut)
                if (response.isSuccessful) {
                    Log.d("MovimientosRepository", "Llamada exitosa. BODY "+response.body())
                    Result.Success(response.body()!!)
                }
                else {
                    Log.e("MovimientosRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("MovimientosRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}