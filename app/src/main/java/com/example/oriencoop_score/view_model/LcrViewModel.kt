package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.LcrResponse
import com.example.oriencoop_score.repository.LcrRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LcrViewModel @Inject constructor(
    private val repository: LcrRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _lcrData = MutableStateFlow<LcrResponse?>(null) // Usamos CuentaCap?, no CuentaCapResponse
    val lcrData: StateFlow<LcrResponse?> = _lcrData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null) // Usamos String?, no String("")
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        LcrDatos()
    }


    fun LcrDatos() {
        val token = sessionManager.token.value ?: "" //Maneja los posibles null
        val rut = sessionManager.username.value ?: "" //Maneja los posibles null
        if (token.isBlank() || rut.isBlank()){ //Comprueba si son blancos
            _error.value = "Token o Rut no pueden estar vacíos"
            Log.e("LcrViewModel", "Token o Rut no pueden estar vacíos")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            Log.d("LcrViewModel", "Token: $token, Rut: $rut")

            try {
                when (val result = repository.getLcr(token, rut)) {
                    is Result.Success -> {
                        _lcrData.value = result.data
                        Log.d("LcrViewModel", "Datos obtenidos: ${result.data}")
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message // Guardar el mensaje de error
                        Log.e("LcrViewModel", "Error al obtener datos: ${result.exception.message}")
                    }
                    Result.Loading -> _isLoading.value = true
                }
            } catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                Log.e("LcrViewModel", "Exception thrown: ${e.message}")
            } finally {
                _isLoading.value = false // Finalizar la carga
            }
        }
    }
}