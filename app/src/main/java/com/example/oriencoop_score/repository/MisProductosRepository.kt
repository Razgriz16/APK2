package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.model.MisProductosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.api.MisProductosService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MisProductosRepository @Inject constructor(private val misProductosService: MisProductosService){


    suspend fun getProductos(token: String, rut: String): Result<MisProductosResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("MisProductosRepository", "llamando función getProductos")
                val response = misProductosService.getProductos(token, rut)
                if (response.isSuccessful) {
                    Log.d("MisProductosRepository", "Llamada exitosa. BODY "+response.body())
                    Result.Success(response.body()!!)
                }
                else {
                    Log.e("MisProductosRepository", "Llamada fallida. Error: ${response.code()} ${response.message()}")
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("MisProductosRepository", "Exception Api call. Error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}