package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.MisProductosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MisProductosService {
    @GET("productos/{rut}")
    suspend fun getProductos(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<MisProductosResponse>
}

