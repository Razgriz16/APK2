package com.example.oriencoop_score.repository

import android.util.Log
import com.example.oriencoop_score.api.ManageMindicatorsApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.api.MindicatorInterface
import com.example.oriencoop_score.model.Indicador
import javax.inject.Inject
import javax.inject.Singleton


class MindicatorsRepository (private val mindicatorsApi: MindicatorInterface){

    suspend fun getIndicadores(): Result<Indicador> {
        return withContext(Dispatchers.IO) {
            try {
                val response = mindicatorsApi.getData()
                if (response.isSuccessful) {
                    val indicadores = response.body()
                    if (indicadores != null) {
                        Log.d("Api call exitosa", "Indicadores: ${indicadores.Dolar}")
                        Result.Success(indicadores)
                    } else {
                        Result.Error(Exception("Response body is null"))
                    }
                } else {
                    Result.Error(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("Api call fallida", "Error: ${e.message}")
                Result.Error(e) // Return error
            }
        }
    }
}