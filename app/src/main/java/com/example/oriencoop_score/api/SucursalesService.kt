package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.ObtenerComunasResponse
import com.example.oriencoop_score.model.MisProductosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SucursalesService {
    @GET("obtener_comunas")
    suspend fun getProductos(
    ): Response<ObtenerComunasResponse>
}