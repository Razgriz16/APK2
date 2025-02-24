package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.CreditoCuotasResponse
import com.example.oriencoop_score.model.HiddenLoginRequest
import com.example.oriencoop_score.model.HiddenLoginResponse
import com.example.oriencoop_score.model.UserLoginRequest
import com.example.oriencoop_score.model.UserLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginService {
    @POST("hidden_login")
    suspend fun hiddenLogin(@Body loginRequest: HiddenLoginRequest ): Response<HiddenLoginResponse>

    @POST("login")
    suspend fun userLogin(
        @Header("Authorization") token: String,
        @Body userLoginRequest: UserLoginRequest
    ): Response<UserLoginResponse>

}
