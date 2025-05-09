package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.FacturasLccResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FacturasService {
    @GET("facturas_lcc/{cuenta}")
    suspend fun getFacturasLcc(
        @Header("Authorization") token: String,
        @Path("cuenta") cuenta: Long
    ): Response<FacturasLccResponse>
}