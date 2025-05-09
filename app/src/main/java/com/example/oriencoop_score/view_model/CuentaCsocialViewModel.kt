package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.CuentaCsocial
import com.example.oriencoop_score.repository.CuentaCsocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CuentaCsocialViewModel @Inject constructor(
    private val repository: CuentaCsocialRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _cuentaCsocialData = MutableStateFlow<ApiResponse<CuentaCsocial>?>(null)
    val cuentaCsocialData: StateFlow<ApiResponse<CuentaCsocial>?> = _cuentaCsocialData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        cuentaCsocialDatos()
    }


    fun cuentaCsocialDatos() {
        val rut = sessionManager.getUserRut().toString()
        val accessToken = sessionManager.getAccessToken().toString()
        if (rut.isBlank()) {
            _error.value = "rut no disponible"
            Log.e("CuentaCsocialViewModel", "Cédula no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("CuentaCsocialViewModel", "Iniciando obtención de datos para cédula: $rut")

            when (val result = repository.fetchProducto(rut, accessToken)) {
                is Result.Success -> {
                    _cuentaCsocialData.value = result.data
                    _error.value = null
                    Log.d("CuentaCsocialViewModel", "Datos obtenidos: ${result.data.data.size} cuentas")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("CuentaCsocialViewModel", "Error al obtener datos: ${result.exception.message}")
                }

                Result.Loading -> Result.Loading
            }
            _isLoading.value = false
        }
    }
}