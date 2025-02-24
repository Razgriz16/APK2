package com.example.oriencoop_score

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    // Estado global del token
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token.asStateFlow()

    // Estado global del username
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    // Guardar el token y username en la sesión
    fun saveSession(token: String, username: String) {
        _token.value = token
        _username.value = username
        Log.d("SessionManager", "Session saved: Token=$token, Username=$username")
    }

    // Limpiar la sesión (logout)
    fun clearSession() {
        _token.value = ""
        _username.value = ""
        Log.d("SessionManager", "Session cleared")

    }
}