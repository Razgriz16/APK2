package com.example.oriencoop_score.model
import com.google.gson.annotations.SerializedName

// Modelo de solicitud de inicio de sesión
data class UserLoginRequest(
    @SerializedName("rut") val rut: String,
    @SerializedName("clave") val clave: String
)

// Respuesta completa de la API
data class AuthResponse(
    @SerializedName("data") val data: List<AuthData>,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String,
    @SerializedName("trace_id") val traceId: String
)

// Elemento del arreglo "data"
data class AuthData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("usuario") val usuario: Usuario
)

// Objeto "usuario"
data class Usuario(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("rut") val rut: Int
)

// Modelo de solicitud de cierre de sesión
data class UserLogoutRequest(
    @SerializedName("token") val token: String
)

// Respuesta de cierre de sesión
data class UserLogoutResponse(
    @SerializedName("message") val message: String
)