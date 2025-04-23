package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.AuthResponse
import com.example.oriencoop_score.model.UserLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    /* *****LOGIN ENTIDAD ANTIGUO*****
    - Hidden login es el login de entidad del cual se obtiene el token que permite seguir navegando

    @POST("hidden_login")
    suspend fun hiddenLogin(@Body loginRequest: HiddenLoginRequest ): Response<HiddenLoginResponse>
    */

    /* *****LOGIN CLIENTE ANTIGUO*****
    - Este es el login que los clientes de la aplicación usarían para poder acceder luego de haber obtenido el
    token del login de entidad.

    @POST("login")
    suspend fun userLogin(
        @Header("Authorization") token: String,
        @Body userLoginRequest: UserLoginRequest
    ): Response<UserLoginResponse>
    */

    /* ***** LOGIN CLIENTE NUEVA API *****
    - Login nuevo de clientes
    - Toma como parametros el rut y la clave y devuelve el token que se usa para las demás llamadas
    */
    @POST("auth/login")
    suspend fun userLogin(
        @Body loginRequest: UserLoginRequest
    ): Response<AuthResponse>

}
