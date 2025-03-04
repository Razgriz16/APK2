package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.model.MovimientosAhorroResponse
import com.example.oriencoop_score.model.MovimientosCreditosResponse
import com.example.oriencoop_score.model.MovimientosLccResponse
import com.example.oriencoop_score.model.MovimientosLcrResponse
import com.example.oriencoop_score.model.MovimientosResponse
import com.example.oriencoop_score.model.RutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("movimientos_creditos/{rut}")
    suspend fun getMovimientosCreditos(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<MovimientosCreditosResponse>

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