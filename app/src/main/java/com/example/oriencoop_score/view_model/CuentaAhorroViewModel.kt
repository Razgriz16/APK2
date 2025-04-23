package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.model.CuentaAhorroResponse
import com.example.oriencoop_score.repository.CuentaAhorroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CuentaAhorroViewModel @Inject constructor(
    private val repository: CuentaAhorroRepository,
    private val sessionManager: SessionManager
) : ViewModel() {


    private val _cuentaAhorroData = MutableStateFlow<CuentaAhorroResponse?>(CuentaAhorroResponse(emptyList()))
    val cuentaAhorroData: StateFlow<CuentaAhorroResponse?> = _cuentaAhorroData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Cuenta seleccionada para expandir detalles
    private val _cuentaSeleccionada = MutableStateFlow<CuentaAhorro?>(null)
    val cuentaSeleccionada: StateFlow<CuentaAhorro?> = _cuentaSeleccionada

    init {
        cuentaAhorroDatos()
    }

    fun cuentaAhorroDatos() {
        val token = sessionManager.getAccessToken() ?: ""
        val rut = sessionManager.getUserRut().toString() ?: ""
        if (token.isBlank() || rut.isBlank()) {
            Log.e("CuentaAhorroViewModel", "Token o Rut no pueden estar vacíos")
            _error.value = "Token o Rut no pueden estar vacíos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("CuentaAhorroViewModel", "Token: $token, Rut: $rut")

            try {
                when (val result = repository.getAhorro(token, rut)) {
                    is Result.Success -> {
                        Log.d("CuentaAhorroViewModel", "Datos obtenidos: ${result.data}")
                        _cuentaAhorroData.value = result.data
                    }
                    is Result.Error -> {
                        Log.e("CuentaAhorroViewModel", "Error al obtener datos: ${result.exception.message}")
                        _error.value = result.exception.message
                    }
                    Result.Loading -> Result.Loading
                }
            } catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                Log.e("CuentaAhorroViewModel", "Exception thrown: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCuenta(cuenta: CuentaAhorro) {
        // Si se toca la misma cuenta, se oculta la vista de detalles (toggle)
        if (_cuentaSeleccionada.value?.NROCUENTA == cuenta.NROCUENTA) {
            _cuentaSeleccionada.value = null
        } else {
            _cuentaSeleccionada.value = cuenta
        }
    }

    fun clearCuentaSeleccionada() {
        _cuentaSeleccionada.value = null
    }
}
