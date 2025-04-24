package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.CiudadesResponse
import com.example.oriencoop_score.model.ComunasResponse
import com.example.oriencoop_score.model.SucursalesResponse
import retrofit2.Response
import retrofit2.http.GET

interface SucursalesService {
    // Endpoint for Ciudades
    @GET("ciudades") // Use the actual endpoint path
    suspend fun getCiudades(): Response<CiudadesResponse>

    // Endpoint for Comunas
    @GET("comunas") // Use the actual endpoint path
    suspend fun getComunas(): Response<ComunasResponse>

    // Endpoint for Sucursales
    @GET("sucursales/activas") // Use the actual endpoint path
    suspend fun getSucursales(): Response<SucursalesResponse>
}
