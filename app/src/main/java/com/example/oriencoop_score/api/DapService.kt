package com.example.oriencoop_score.api

import DapResponse
import com.example.oriencoop_score.model.ClienteInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DapService {
    @GET("dap/{rut}")
    suspend fun getDap(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<DapResponse>
}