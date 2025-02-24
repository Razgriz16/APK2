package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.LccResponse
import com.example.oriencoop_score.model.RutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface LccService {
    @GET("lcc/{rut}")
    suspend fun getLcc(
        @Header("Authorization") token: String,
        @Path("rut") rut: String
    ): Response<LccResponse>
}

