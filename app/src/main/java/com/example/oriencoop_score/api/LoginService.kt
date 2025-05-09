package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.AuthResponse
import com.example.oriencoop_score.model.UserLoginRequest
import com.example.oriencoop_score.model.UserLogoutRequest
import com.example.oriencoop_score.model.UserLogoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    /* ***** LOGIN CLIENTE NUEVA API *****
    - Login nuevo de clientes
    - Toma como parametros el rut y la clave y devuelve el token que se usa para las demás llamadas
    */
    @POST("auth/login")
    suspend fun userLogin(
        @Body loginRequest: UserLoginRequest
    ): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun userLogout(
        @Body token: UserLogoutRequest
    ): Response<ApiResponse<UserLogoutResponse>>

}
