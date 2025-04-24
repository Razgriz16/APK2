package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.MovimientosAhorroResponse
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.model.MovimientosLccResponse
import com.example.oriencoop_score.model.MovimientosLcrResponse
import com.example.oriencoop_score.model.MovimientosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovimientosService {
    @GET("movimientos/{rut}")
    suspend fun getMovimientos(
        @Header("Authorization") token: String,
        @Path ("rut") rut: String
    ): Response<MovimientosResponse>

    @GET("movimientos_ahorro/{rut}")
    suspend fun getMovimientosAhorro(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<MovimientosAhorroResponse>

    @GET("creditos/movimientos")
    suspend fun getMovimientosCreditos(
        @Query("credito") credito: Long
    ): Response<ApiResponse<MovimientosCreditos>>

    @GET("movimientos_lcc/{rut}")
    suspend fun getMovimientosLcc(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<MovimientosLccResponse>

    @GET("movimientos_lcr/{rut}")
    suspend fun getMovimientosLcr(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<MovimientosLcrResponse>
}