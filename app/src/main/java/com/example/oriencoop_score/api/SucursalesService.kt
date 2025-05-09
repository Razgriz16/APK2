package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.CiudadesResponse
import com.example.oriencoop_score.model.ComunasResponse
import com.example.oriencoop_score.model.SucursalesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SucursalesService {
    /**
     * Estructura de los endpoints: http://ip:port/v1/{api a la que se quiere ir}/{ruta dentro de la api}
     * Ejemplo: http://192.168.120.8:8001/v1/parametro/comunas
     */

    // Endpoint for Ciudades
    @GET("ciudades") // Use the actual endpoint path
    suspend fun getCiudades(
        @Header("Authorization") token: String
    ): Response<CiudadesResponse>

    // Endpoint for Comunas
    @GET("comunas") // Use the actual endpoint path
    suspend fun getComunas(
        @Header("Authorization") token: String
    ): Response<ComunasResponse>


    // Endpoint for Sucursales
    @GET("sucursales/activas") // Use the actual endpoint path
    suspend fun getSucursales(
        @Header("Authorization") token: String
    ): Response<SucursalesResponse>
}
