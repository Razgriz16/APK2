package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.AddressResponse
import com.example.oriencoop_score.model.Mail
import com.example.oriencoop_score.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClienteService {

    @GET("cliente")
    suspend fun getClienteInfo(
        @Query("rut") rut: String
    ): Response<UserResponse>

    @GET("cliente/correo")
    suspend fun getMail(
        @Query("rut") rut: String
    ): Response<Mail>

    @GET("cliente/direccion")
    suspend fun getAddress(
        @Query("rut") rut: String
    ): Response<AddressResponse>
}