package com.example.oriencoop_score.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.oriencoop_score.model.Usuario
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        try {
            val masterKeyAlias = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedSharedPreferences.create(
                context,
                "session_prefs",
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.e("SessionManager", "Failed to initialize EncryptedSharedPreferences", e)
            context.getSharedPreferences("session_prefs_fallback", Context.MODE_PRIVATE)
        }
    }

    private val _sessionState = MutableStateFlow(getSessionState())
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    data class SessionState(
        val isLoggedIn: Boolean,
        val accessToken: String?,
        val refreshToken: String?,
        val userName: String?,
        val userRut: Int,
        val nroCuenta: Long
    )

    private fun getSessionState(): SessionState {
        return SessionState(
            isLoggedIn = isLoggedIn(),
            accessToken = getAccessToken(),
            refreshToken = getRefreshToken(),
            userName = getUserName(),
            userRut = getUserRut(),
            nroCuenta = getNroCuenta()
        )
    }

    fun saveSession(accessToken: String, refreshToken: String, usuario: Usuario) {
        with(sharedPreferences.edit()) {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            putString("user_nombre", usuario.nombre)
            putInt("user_rut", usuario.rut)
            apply()
        }
        _sessionState.value = getSessionState()
        Log.d("SessionManager", "Session saved successfully")
    }

    fun clearSession() {
        sharedPreferences.edit() { clear() }

        _sessionState.value = getSessionState()
        Log.d("SessionManager", "Session cleared")
    }

    fun saveNroCuenta(nroCuenta: Long) {
        with(sharedPreferences.edit()) {
            putLong("nro_cuenta", nroCuenta)
            apply()
        }
        Log.d("SessionManager", "NroCuenta saved: $nroCuenta")
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
        Log.d("SessionManager", "Tokens updated")
    }

    fun getAccessToken(): String? = sharedPreferences.getString("access_token", null)

    fun getRefreshToken(): String? = sharedPreferences.getString("refresh_token", null)

    fun getUserName(): String? = sharedPreferences.getString("user_nombre", null)

    fun getUserRut(): Int = sharedPreferences.getInt("user_rut", 0)

    fun getNroCuenta(): Long = sharedPreferences.getLong("nro_cuenta", 0L)

    fun isLoggedIn(): Boolean = getAccessToken() != null // Función no usada
}