package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.model.RutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface CuentaCapService {
    @GET("cuenta_cap/{rut}")
    suspend fun getCuentaCap(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<CuentaCapResponse>
}

