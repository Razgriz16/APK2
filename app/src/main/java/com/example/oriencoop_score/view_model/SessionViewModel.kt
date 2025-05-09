package com.example.oriencoop_score.view_model

import androidx.lifecycle.ViewModel
import com.example.oriencoop_score.model.Usuario
import com.example.oriencoop_score.auth.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    // Exponer el estado reactivo de la sesión
    val sessionState = sessionManager.sessionState

    // Métodos para gestionar la sesión
    fun saveSession(accessToken: String, refreshToken: String, usuario: Usuario) {
        sessionManager.saveSession(accessToken, refreshToken, usuario)
    }

    fun saveNroCuenta(nroCuenta: Long) {
        sessionManager.saveNroCuenta(nroCuenta)
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        sessionManager.updateTokens(accessToken, refreshToken)
    }

    fun clearSession() {
        sessionManager.clearSession()
    }

    // Métodos para obtener datos de la sesión
    fun getAccessToken(): String? = sessionManager.getAccessToken()

    fun getRefreshToken(): String? = sessionManager.getRefreshToken()

    fun getUserName(): String? = sessionManager.getUserName()

    fun getUserRut(): Int = sessionManager.getUserRut()

    fun getNroCuenta(): Long = sessionManager.getNroCuenta()

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
}