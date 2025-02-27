package com.example.oriencoop_score.utility
import com.example.oriencoop_score.model.UserLoginResponse

// Sealed class to represent different states of the login process
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val data: UserLoginResponse) : LoginState()
    data class Error(val message: String?) : LoginState()
}