package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.MovimientosService
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.model.MovimientosCreditosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientosCreditosRepository @Inject constructor(private val movimientosCreditosService: MovimientosService){

    suspend fun getMovimientosCreditos(token: String, rut: String): Result<MovimientosCreditosResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MovimientosCreditosRepository", "llamando función getMovimientosAhorro")
                val response = movimientosCreditosService.getMovimientosCreditos(token, rut)
                if (response.isSuccessful) {
                    Log.d("MovimientosCreditosRepository", "Llamada exitosa. BODY "+response.body())
                    Result.Success(response.body()!!)
                }
                else {
                    Log.e("MovimientosCreditosRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("MovimientosCreditosRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}