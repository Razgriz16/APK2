package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.Indicador
import retrofit2.Response
import retrofit2.http.GET

interface MindicatorInterface {
    @GET("resources/json/indicadores.php")
    suspend fun getData(): Response<Indicador>
}