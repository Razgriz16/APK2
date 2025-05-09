package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.MovimientosCsocial
import com.example.oriencoop_score.repository.MovimientosCsocialRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.MovimientosAhorro
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosCsocialViewModel @Inject constructor(
    private val repository: MovimientosCsocialRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado para los datos de movimientos de cuenta social
    private val _movimientosData = MutableStateFlow<ApiResponse<MovimientosCsocial>?>(null)
    val movimientosData: StateFlow<ApiResponse<MovimientosCsocial>?> = _movimientosData.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Carga los movimientos de cuenta social para un número de cuenta específico.
     *
     * @param numeroCuenta El número de cuenta para obtener los movimientos.
     * @param cantidad La cantidad de movimientos a obtener.
     */
    fun fetchMovimientosCsocial(numeroCuenta: Long) {
        val rut = sessionManager.getUserRut().toString()
        val accessToken = sessionManager.getAccessToken().toString()
        if (rut.isBlank()) {
            _error.value = "RUT no disponible"
            Log.e("MovimientosCsocialViewModel", "RUT no disponible")
            return
        }
        if (accessToken.isBlank()) {
            _error.value = "Token de autenticación no disponible"
            Log.e("MovimientosCsocialViewModel", "Token de autenticación no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("MovimientosCsocialViewModel", "Iniciando obtención de movimientos para cuenta: $numeroCuenta, RUT: $rut")

            when (val result = repository.getMovimientosCsocial(numeroCuenta, accessToken)) {
                is Result.Success -> {
                    _movimientosData.value = ApiResponse<MovimientosCsocial>(
                        count = result.data.count,
                        data = result.data.data,
                        error_code = result.data.error_code,
                    )
                    _error.value = null
                    Log.d("MovimientosCsocialViewModel", "Movimientos obtenidos: ${result.data.count} movimientos")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("MovimientosCsocialViewModel", "Error al obtener movimientos: ${result.exception.message}")
                }

                Result.Loading -> TODO()
            }
            _isLoading.value = false
        }
    }

    /**
     * Limpia los datos de movimientos y errores.
     */
    fun clearMovimientos() {
        _movimientosData.value = null
        _error.value = null
        _isLoading.value = false
    }
}