package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.api.CreditoCuotasService
import com.example.oriencoop_score.model.CreditoCuotasResponse
import com.example.oriencoop_score.model.CuentaAhorroResponse
import com.example.oriencoop_score.model.RutRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditoCuotasRepository @Inject constructor(private val creditoCuotasService: CreditoCuotasService){
    suspend fun getCreditoCuotas(token: String, rut: String): Result<CreditoCuotasResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("CreditoCuotasRepository", "llamando función getCreditoCuotas")
                val response = creditoCuotasService.getCreditoCuotas(token, rut)
                if (response.isSuccessful) {
                    Log.d("CreditoCuotasRepository", "Llamada exitosa. BODY " + response.body())
                    Result.Success(response.body()!!)
                } else {
                    Log.e(
                        "CreditoCuotasRepository",
                        "Llamada fallida. Error: ${response.code()} ${response.message()}"
                    )
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("CreditoCuotasRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}