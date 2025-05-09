package com.example.oriencoop_score.repository

import com.example.oriencoop_score.api.LoginService
import com.example.oriencoop_score.auth.SessionManager
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.Authenticator
import javax.inject.Inject
import javax.inject.Singleton
/*
@Singleton
class TokenAuthenticator @Inject constructor(
    private val sessionManager: SessionManager,
    private val authService: LoginService
) : Authenticator() {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = sessionManager.getRefreshToken() ?: return null
        return try {
            val refreshResponse = authService.refreshToken(refreshToken).execute()
            if (refreshResponse.isSuccessful) {
                val newToken = refreshResponse.body()?.accessToken
                if (newToken != null) {
                    sessionManager.updateTokens(newToken, refreshResponse.body()?.refreshToken ?: refreshToken)
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                } else null
            } else {
                sessionManager.clearSession()
                null
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "Error refreshing token", e)
            null
        }
    }
}*/