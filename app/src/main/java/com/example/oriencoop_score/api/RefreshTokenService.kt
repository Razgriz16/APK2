package com.example.oriencoop_score.api

import com.example.oriencoop_score.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/* si no funciona el refresh puede ser porque la respuesta de la api no coincide con el formato acá.
el "count" en ApiResponse no está en la respuesta del refrescar
 */
interface RefreshTokenService {
    @Headers("Content-type: application/json")
    @POST("auth/refrescar-token")
    suspend fun refreshToken(
        @Body token: RefreshTokenRequest
    ): ApiResponse<RefreshTokenResponse>
}

data class RefreshTokenRequest(val refreshToken: String)
data class RefreshTokenResponse(val accessToken: String, val refreshToken: String)