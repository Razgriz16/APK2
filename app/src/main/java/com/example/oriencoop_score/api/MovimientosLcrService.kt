package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.MovimientosLcrResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MovimientosLcrService {
    @GET("movimientos_lcr/{rut}")
    suspend fun getMovimientosLcr(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<MovimientosLcrResponse>
}