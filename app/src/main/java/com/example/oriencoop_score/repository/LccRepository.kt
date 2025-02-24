package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.api.LccService
import com.example.oriencoop_score.model.CuentaAhorroResponse
import com.example.oriencoop_score.model.LccResponse
import com.example.oriencoop_score.model.RutRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LccRepository @Inject constructor(private val lccService: LccService) {
    suspend fun getLcc(token: String, rut: String): Result<LccResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("LccRepository", "llamando función getAhorro")
                val response = lccService.getLcc(token, rut)
                if (response.isSuccessful) {
                    Log.d("LccRepository", "Llamada exitosa. BODY " + response.body())
                    Result.Success(response.body()!!)
                } else {
                    Log.e("LccRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}"
                    )
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("LccRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}