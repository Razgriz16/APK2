package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.MisProductosCredito
import com.example.oriencoop_score.api.MisProductosService
import com.example.oriencoop_score.model.DapResponse
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.customDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
// DapRepository.kt
@Singleton
class DapRepository @Inject constructor(
    @MisProductosCredito private val dapService: MisProductosService) {
    suspend fun getDap(token: String, rut: String): Result<List<DapResponse>> { // Return Result<List<DapResponse>>
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DapRepository", "llamando función getDap")
                val response = dapService.getDap(token, rut)
                if (response.isSuccessful) {
                    
                    val formattedDaps = response.body()?.map { dapResponse -> // Use map on the list
                        dapResponse.copy( // Use .copy on each DapResponse object
                            fechaActivacion = customDateFormat(dapResponse.fechaActivacion),
                            fechaCreacion = customDateFormat(dapResponse.fechaCreacion),
                            fechaLiquidacion = customDateFormat(dapResponse.fechaLiquidacion),
                            fechaModificacion = customDateFormat(dapResponse.fechaModificacion),
                            fechaRenovacion = customDateFormat(dapResponse.fechaRenovacion),
                            fechaVencimiento = customDateFormat(dapResponse.fechaVencimiento)
                        )
                    }
                    Log.d("DapRepository", "Llamada exitosa. BODY " + response.body())
                    Result.Success(formattedDaps ?: emptyList()) // Return the formatted list
                } else {
                    Log.e("DapRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}"
                    )
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("DapRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}