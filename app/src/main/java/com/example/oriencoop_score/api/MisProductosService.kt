package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.CreditoCuotas
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.model.Lcc
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.CuentaCsocial
import com.example.oriencoop_score.model.Dap
import com.example.oriencoop_score.model.Lcr
import com.example.oriencoop_score.utility.getCurrentDate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface MisProductosService {

    /**
     * Estructura de los endpoints: http://ip:port/v1/{api a la que se quiere ir}/{ruta dentro de la api}
     * Ejemplo: http://192.168.120.8:8001/v1/parametro/comunas
     */
    @GET("creditos")
    suspend fun getCreditoCuotas(
        @Query("rut") rut: String,
        @Query("estado") estado: Int = 2,
        @Header("Authorization") token: String
    ): Response<ApiResponse<CreditoCuotas>>

    @GET("cuenta-ahorro")
    suspend fun getAhorro(
        @Query("rut-titular") rut: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<CuentaAhorro>>

    @GET("cuenta-csocial")
    suspend fun getCuentaCsocial(
        @Query("rut-titular") rut: String,
        @Header("Authorization") token: String,
    ): Response<ApiResponse<CuentaCsocial>>

    @GET("recuperar-dap")
    suspend fun getDap(
        @Query("rut_cliente") rut: String,
        @Header("Authorization") token: String,
        @Query("estado") estado: Int = 20,
        @Query("fecha_inicio") fecha_inicio: String = "01-01-2005",
        @Query("fecha_fin") fecha_fin: String = getCurrentDate(),
        @Query("indicador1") indicador1: String = "S",
        @Query("indicador2") indicador2: String = "S",

    ): Response<ApiResponse<Dap>>

    @GET("lineacc")
    suspend fun getLcc(
        @Query("rut") rut: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Lcc>>

    @GET("lineacr")
    suspend fun getLcr(
        @Query("rut") rut: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Lcr>>
}

