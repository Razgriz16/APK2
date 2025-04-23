package com.example.oriencoop_score.model
import com.google.gson.annotations.SerializedName

/* ***** Login entidad antiguo *****
data class HiddenLoginRequest(val username: String, val password: String)
data class HiddenLoginResponse(val message: String, val token: String)
*/

/* ***** Login clientes antiguo *****
data class UserLoginRequest(val rut: String, val password: String)
data class UserLoginResponse(val message: String, val rut: String)
*/


data class UserLoginRequest(val rut: String, val clave: String)

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