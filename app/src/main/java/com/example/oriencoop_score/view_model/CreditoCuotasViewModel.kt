package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.model.CreditoCuota
import com.example.oriencoop_score.model.CreditoCuotasResponse
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.model.CuentaCapResponse
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.repository.CreditoCuotasRepository
import com.example.oriencoop_score.repository.CuentaCapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditoCuotasViewModel @Inject constructor(
    private val repository: CreditoCuotasRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _creditoCuotasData = MutableStateFlow<CreditoCuotasResponse?>(CreditoCuotasResponse(
        emptyList())) // Usamos CuentaCap?, no CuentaCapResponse
    val creditoCuotasData: StateFlow<CreditoCuotasResponse?> = _creditoCuotasData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null) // Usamos String?, no String("")
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Cuenta seleccionada para expandir detalles
    private val _cuentaSeleccionada = MutableStateFlow<CreditoCuota?>(null)
    val cuentaSeleccionada: StateFlow<CreditoCuota?> = _cuentaSeleccionada

    init {
        creditoCuotasDatos()
    }


    fun creditoCuotasDatos() {
        val token = sessionManager.token.value ?: "" // Maneja los posibles null
        val rut = sessionManager.username.value ?: "" // Maneja los posibles null
        if (token.isBlank() || rut.isBlank()) { // Comprueba si son blancos
            _error.value = "Token o Rut no pueden estar vacíos"
            Log.e("CreditoCuotasViewModel", "Token o Rut no pueden estar vacíos")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            Log.d("CreditoCuotasViewModel", "Token: $token, Rut: $rut")

            try {
                when (val result = repository.getCreditoCuotas(token, rut)) {
                    is Result.Success -> {
                        _creditoCuotasData.value = result.data
                        Log.d("CreditoCuotasViewModel", "Datos obtenidos: ${result.data}")
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message // Guardar el mensaje de error
                        Log.e("CreditoCuotasViewModel", "Error al obtener datos: ${result.exception.message}")
                    }
                    Result.Loading -> _isLoading.value = false
                }
            } catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                Log.e("CreditoCuotasViewModel", "Exception thrown: ${e.message}")
            } finally {
                _isLoading.value = false // Finalizar la carga
            }
        }
    }

    fun selectCuenta(cuenta: CreditoCuota) {
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