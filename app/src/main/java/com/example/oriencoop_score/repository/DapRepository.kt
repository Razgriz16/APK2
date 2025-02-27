package com.example.oriencoop_score.repository

import DapResponse
import android.util.Log
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.api.DapService
import com.example.oriencoop_score.utility.toFormattedDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DapRepository @Inject constructor(private val dapService: DapService){
    suspend fun getDap(token: String, rut: String): Result<DapResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("DapRepository", "llamando función getAhorro")
                val response = dapService.getDap(token, rut)
                if (response.isSuccessful) {
                    val formattedDates = response.body()?.dapResponse?.map { dap -> // Access the list within DapResponse
                        dap.copy(
                            fechaActivacion = dap.fechaActivacion.toFormattedDate(),
                            fechaCreacion = dap.fechaCreacion.toFormattedDate(),
                            fechaLiquidacion = dap.fechaLiquidacion.toFormattedDate(),
                            fechaModificacion = dap.fechaModificacion.toFormattedDate(),
                            fechaRenovacion = dap.fechaRenovacion.toFormattedDate(),
                            fechaVencimiento = dap.fechaVencimiento.toFormattedDate()
                        )
                    }
                    Log.d("DapRepository", "Llamada exitosa. BODY " + response.body())
                    // Wrap the transformed list in a DapResponse object
                    Result.Success(DapResponse(formattedDates ?: emptyList()))
                } else {
                    Log.e(
                        "DapRepository",
                        "Llamada fallida. Error: ${response.code()} ${response.message()}"
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