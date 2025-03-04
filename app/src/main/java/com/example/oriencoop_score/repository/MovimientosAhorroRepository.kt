package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.MovimientosService
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.model.MovimientosAhorroResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientosAhorroRepository @Inject constructor(private val movimientosAhorroService: MovimientosService){


    suspend fun getMovimientosAhorro(token: String, rut: String): Result<MovimientosAhorroResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MovimientosAhorroRepository", "llamando función getMovimientosAhorro")
                val response = movimientosAhorroService.getMovimientosAhorro(token, rut)
                if (response.isSuccessful) {
                    Log.d("MovimientosAhorroRepository", "Llamada exitosa. BODY "+response.body())
                    Result.Success(response.body()!!)
                }
                else {
                    Log.e("MovimientosAhorroRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("MovimientosAhorroRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}