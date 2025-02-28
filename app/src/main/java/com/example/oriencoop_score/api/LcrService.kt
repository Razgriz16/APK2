package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.LcrResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface LcrService {
    @GET("lcr/{rut}")
    suspend fun getLcr(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<LcrResponse>
}