package com.example.oriencoop_score.repository

import android.util.Log

import com.example.oriencoop_score.api.LoginService

import com.example.oriencoop_score.model.HiddenLoginRequest
import com.example.oriencoop_score.model.HiddenLoginResponse
import com.example.oriencoop_score.model.UserLoginRequest
import com.example.oriencoop_score.model.UserLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.example.oriencoop_score.Result
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val loginService: LoginService) {
    suspend fun performHiddenLogin(
        username: String,
        password: String
    ): Result<HiddenLoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("Login", "antes de realizando llamada a api")
                val response = loginService.hiddenLogin(HiddenLoginRequest(username, password))
                Log.d("Login", "despues de realizar llamada a api")
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Log.d("Login", "Hidden login successful: ${data.token}")
                        Result.Success(data)
                    } else {
                        Log.e("LoginError", "Response body is null")
                        Result.Error(Exception("Response body is null"))
                    }
                } else {
                    Log.e("LoginError", "Hidden login failed: ${response.code()} - ${response.errorBody()?.string()}")
                    throw Exception("Hidden login failed")
                }
            } catch (e: IOException) {
                Log.e("Retrofit", "Network error: ${e.message}")
                Result.Error(e)
            }

        }
    }

    suspend fun performUserLogin(
        token: String,
        rut: String,
        password: String
    ): Result<UserLoginResponse> {
        return withContext(Dispatchers.IO) {
            Log.d("PerformUserLogin", "Realizando llamada a api")
            val response = loginService.userLogin(token, UserLoginRequest(rut, password))
            try {
                if (response.isSuccessful) {
                    Log.d("PerformUserLogin", "Llamada a api exitosa")
                    val data = response.body()
                    if (data != null) {
                        Log.d("PerformUserLogin ${data.rut}", "mensaje:${data.message}")
                        Result.Success(data)
                    } else {
                        Log.e("PerformUserLogin", "Response body is null")
                        Result.Error(Exception("Response body is null"))
                    }
                } else {
                    Log.e("PerformUserLogin", "Hidden login failed: ${response.code()} - ${response.errorBody()?.string()}")
                    throw Exception("Hidden login failed")
                }
            } catch (e: IOException) {
                Log.e("PerformUserLogin", "Network error: ${e.message}")
                Result.Error(e)
            }
        }
    }
}