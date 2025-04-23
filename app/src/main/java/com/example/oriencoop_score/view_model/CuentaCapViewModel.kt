package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.repository.CuentaCapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CuentaCapViewModel @Inject constructor(
    private val repository: CuentaCapRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

        private val _cuentaCapData = MutableStateFlow<CuentaCapResponse?>(null) // Usamos CuentaCap?, no CuentaCapResponse
        val cuentaCapData: StateFlow<CuentaCapResponse?> = _cuentaCapData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null) // Usamos String?, no String("")
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        cuentaCapDatos()
    }


    fun cuentaCapDatos() {
        val token = sessionManager.getAccessToken() ?: "" // Maneja los posibles null
        val rut = sessionManager.getUserRut().toString() ?: "" // Maneja los posibles null
        if (token.isBlank() || rut.isBlank()) { // Comprueba si son blancos
            _error.value = "Token o Rut no pueden estar vacíos"
            Log.e("CuentaCapViewModel", "Token o Rut no pueden estar vacíos")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            Log.d("CuentaCapViewModel", "Token: $token, Rut: $rut")

            try {
                when (val result = repository.getCuentaCap(token, rut)) {
                    is Result.Success -> {
                        _cuentaCapData.value = result.data
                        Log.d("CuentaCapViewModel", "Datos obtenidos: ${result.data}")
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message // Guardar el mensaje de error
                        Log.e("CuentaCapViewModel", "Error al obtener datos: ${result.exception.message}")
                    }
                    Result.Loading -> _isLoading.value = true
                }
            } catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                Log.e("CuentaCapViewModel", "Exception thrown: ${e.message}")
            } finally {
                _isLoading.value = false // Finalizar la carga
            }
        }
    }
}