package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.CreditoCuotas
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.model.DapResponse
import com.example.oriencoop_score.model.LccResponse
import com.example.oriencoop_score.model.LcrResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MisProductosService {

    @GET("creditos")
    suspend fun getCreditoCuotas(
        @Query("rut") rut: String,
        @Query("estado") estado: Int = 2
    ): Response<ApiResponse<CreditoCuotas>>

    @GET("cuenta-ahorro")
    suspend fun getAhorro(
        @Query("rut-titular") rut: String,
    ): Response<ApiResponse<CuentaAhorro>>

    @GET("cuenta_cap/{rut}")
    suspend fun getCuentaCap(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<CuentaCapResponse>

    @GET("dap/{rut}")
    suspend fun getDap(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<List<DapResponse>>

    @GET("lcc/{rut}")
    suspend fun getLcc(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<LccResponse>

    @GET("lcr/{rut}")
    suspend fun getLcr(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<LcrResponse>
}

