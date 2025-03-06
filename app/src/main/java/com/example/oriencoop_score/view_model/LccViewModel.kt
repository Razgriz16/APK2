package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.LccResponse
import com.example.oriencoop_score.repository.LccRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LccViewModel @Inject constructor(
    private val repository: LccRepository,
    private val sessionManager: SessionManager,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    companion object {
        const val NRO_CUENTA_KEY = "nroCuenta"
    }

    private val _lccData = MutableStateFlow<LccResponse?>(LccResponse(emptyList())) // Usamos CuentaCap?, no CuentaCapResponse
    val lccData: StateFlow<LccResponse?> = _lccData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null) // Usamos String?, no String("")
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading



    init {
        Log.d("LccViewModel", "LccViewModel initialized!")
        LccDatos()
    }

    fun LccDatos() {
        val token = sessionManager.token.value ?: "" //Maneja los posibles null
        val rut = sessionManager.username.value ?: "" //Maneja los posibles null
        if (token.isBlank() || rut.isBlank()){ //Comprueba si son blancos
            _error.value = "Token o Rut no pueden estar vacíos"
            Log.e("LccViewModel", "Token o Rut no pueden estar vacíos")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            Log.d("LccViewModel", "Token: $token, Rut: $rut")

            try {
                when (val result = repository.getLcc(token, rut)) {
                    is Result.Success -> {
                        _lccData.value = result.data
                        // Use result.data directly
                        val nroCuenta = result.data.lcc.firstOrNull()?.numerocuenta
                        Log.d("LccViewModel", "NROCUENTA: $nroCuenta")
                        if (nroCuenta != null) {
                            sessionManager.setNroCuenta(nroCuenta)
                        }
                        Log.d("LccViewModel", "Datos obtenidos: ${result.data}")
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message // Guardar el mensaje de error
                        Log.e("LccViewModel", "Error al obtener datos: ${result.exception.message}")
                    }
                    Result.Loading -> _isLoading.value = true
                }
            } catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                Log.e("LccViewModel", "Exception thrown: ${e.message}")
            } finally {
                _isLoading.value = false // Finalizar la carga
            }
        }
    }
}