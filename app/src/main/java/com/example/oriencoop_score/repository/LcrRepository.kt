package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.LcrService
import com.example.oriencoop_score.model.LcrResponse
import com.example.oriencoop_score.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LcrRepository @Inject constructor(private val lcrService: LcrService) {
    suspend fun getLcr(token: String, rut: String): Result<LcrResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("LcrRepository", "llamando función getLcr")
                val response = lcrService.getLcr(token, rut)
                if (response.isSuccessful) {
                    Log.d("LcrRepository", "Llamada exitosa. BODY " + response.body())
                    Result.Success(response.body()!!)
                } else {
                    Log.e("LcrRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}"
                    )
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("LcrRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}