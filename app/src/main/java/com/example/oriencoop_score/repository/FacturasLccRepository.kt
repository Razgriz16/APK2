package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.FacturasService
import com.example.oriencoop_score.api.MovimientosService
import com.example.oriencoop_score.model.FacturasLccResponse
import com.example.oriencoop_score.model.MovimientosLccResponse
import com.example.oriencoop_score.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacturasLccRepository @Inject constructor(private val facturasLccService: FacturasService) {
    suspend fun getFacturasLcc(token: String, cuenta: Long): Result<FacturasLccResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("FacturasLccRepository", "llamando función getFacturasLcc")
                val response = facturasLccService.getFacturasLcc(token, cuenta)
                if (response.isSuccessful) {
                    Log.d("FacturasLccRepository", "Llamada exitosa. BODY "+response.body())
                    Result.Success(response.body()!!)
                }
                else {
                    Log.e("FacturasLccRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("FacturasLccRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}