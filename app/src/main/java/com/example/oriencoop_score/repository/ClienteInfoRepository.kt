package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.api.ClienteInfoService
import com.example.oriencoop_score.model.ClienteInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClienteInfoRepository @Inject constructor(private val clienteInfoService: ClienteInfoService) {
    suspend fun getClienteInfo(token: String, rut: String): Result<ClienteInfoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ClienteInfoRepository", "llamando función getClienteInfo")
                val response = clienteInfoService.getClienteInfo(token, rut)
                if (response.isSuccessful) {
                    Log.d("ClienteInfoRepository", "Llamada exitosa. BODY " + response.body())
                    Result.Success(response.body()!!)
                } else {
                    Log.e(
                        "ClienteInfoRepository",
                        "Llamada fallida. Error: ${response.code()} ${response.message()}"
                    )
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("ClienteInfoRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}