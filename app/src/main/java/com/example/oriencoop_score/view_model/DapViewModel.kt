package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.model.DapResponse
import com.example.oriencoop_score.repository.CuentaCapRepository
import com.example.oriencoop_score.repository.DapRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DapViewModel @Inject constructor(
    private val repository: DapRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _dapData = MutableStateFlow<List<DapResponse?>>(emptyList())
    val dapData: StateFlow<List<DapResponse?>> = _dapData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _cuentaSeleccionada = MutableStateFlow<DapResponse?>(null)
    val cuentaSeleccionada: StateFlow<DapResponse?> = _cuentaSeleccionada


    init {
        dapDatos()
    }


    fun dapDatos() {
        val token = sessionManager.getAccessToken().toString() // Maneja los posibles null
        val rut = sessionManager.getUserRut().toString() // Maneja los posibles null
        if (token.isBlank() || rut.isBlank()) { // Comprueba si son blancos
            _error.value = "Token o Rut no pueden estar vacíos"
            Log.e("DapViewModel", "Token o Rut no pueden estar vacíos")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            Log.d("DapViewModel", "Token: $token, Rut: $rut")

            try {
                when (val result = repository.getDap(token, rut)) {
                    is Result.Success -> {
                        _dapData.value = result.data
                        Log.d("DapViewModel", "Datos obtenidos: ${result.data}")
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message // Guardar el mensaje de error
                        Log.e("DapViewModel", "Error al obtener datos: ${result.exception.message}")
                    }
                    Result.Loading -> _isLoading.value = true
                }
            } catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                Log.e("DapViewModel", "Exception thrown: ${e.message}")
            } finally {
                _isLoading.value = false // Finalizar la carga
            }
        }
    }

    fun selectCuenta(cuenta: DapResponse) {
        // Si se toca la misma cuenta, se oculta la vista de detalles (toggle)
        if (_cuentaSeleccionada.value?.numeroDeposito == cuenta.numeroDeposito) {
            _cuentaSeleccionada.value = null
        } else {
            _cuentaSeleccionada.value = cuenta
        }
    }

}
