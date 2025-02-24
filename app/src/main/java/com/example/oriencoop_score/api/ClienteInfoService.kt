package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.ClienteInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ClienteInfoService {
    @GET("cliente/{rut}")
    suspend fun getClienteInfo(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<ClienteInfoResponse>
}