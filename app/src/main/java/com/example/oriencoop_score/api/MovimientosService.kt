package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.MovimientosAhorro
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.model.MovimientosLccResponse
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.MovimientosCsocial
import com.example.oriencoop_score.model.MovimientosLcr
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovimientosService {

    /**
     * Estructura de los endpoints: http://ip:port/v1/{api a la que se quiere ir}/{ruta dentro de la api}
     * Ejemplo: http://192.168.120.8:8001/v1/parametro/comunas
     *                                  (de api-parametro)
     */
    @GET("cuenta-csocial/ultimos-movimientos")
    suspend fun getMovimientosCsocial(
        @Query("numero-cuenta") numero_cuenta: Long,
        @Header("Authorization") token: String
    ): Response<ApiResponse<MovimientosCsocial>>

    @GET("cuenta-ahorro/ultimos-movimientos")
    suspend fun getMovimientosAhorro(
        @Query("numero-cuenta") numero_cuenta: Int,
        @Query("cantidad") cantidad: Int = 20,
        @Header("Authorization") token: String
    ): Response<ApiResponse<MovimientosAhorro>>

    @GET("creditos/movimientos")
    suspend fun getMovimientosCreditos(
        @Query("credito") credito: Long
    ): Response<ApiResponse<MovimientosCreditos>>

    @GET("movimientos-lcc/{rut}")
    suspend fun getMovimientosLcc(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<MovimientosLccResponse>

    @GET("movimientos-lineacr")
    suspend fun getMovimientosLcr(
        @Query("nro_cuenta") numero_cuenta: Int,
        @Query("cantidad") cantidad: Int = 20,
        @Header("Authorization") token: String
    ): Response<ApiResponse<MovimientosLcr>>
}