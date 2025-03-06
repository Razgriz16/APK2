package com.example.oriencoop_score.utility

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _nroCuenta = MutableStateFlow<Long>(0)
    val nroCuenta: StateFlow<Long> = _nroCuenta

    fun setNroCuenta(nroCuenta: Long) {
        Log.d("SessionManager", "Setting nroCuenta to: $nroCuenta")
        _nroCuenta.value = nroCuenta
    }

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