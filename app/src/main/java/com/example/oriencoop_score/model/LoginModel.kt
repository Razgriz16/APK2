package com.example.oriencoop_score.model

data class HiddenLoginRequest(val username: String, val password: String)
data class HiddenLoginResponse(val message: String, val token: String)

data class UserLoginRequest(val rut: String, val password: String)
data class UserLoginResponse(val message: String, val rut: String)

