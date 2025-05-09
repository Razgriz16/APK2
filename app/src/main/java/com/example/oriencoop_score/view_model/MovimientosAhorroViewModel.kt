package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.MovimientosAhorro
import com.example.oriencoop_score.repository.MovimientosAhorroRepository
import com.example.oriencoop_score.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosAhorroViewModel @Inject constructor(
    private val repository: MovimientosAhorroRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado para los datos de movimientos de ahorro
    private val _movimientosData = MutableStateFlow<ApiResponse<MovimientosAhorro>?>(null)
    val movimientosData: StateFlow<ApiResponse<MovimientosAhorro>?> = _movimientosData.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Carga los movimientos de ahorro para un número de cuenta específico.
     *
     * @param numeroCuenta El número de cuenta (ahorro) para obtener los movimientos.
     * @param cantidad Cantidad de movimientos a obtener (por defecto 20).
     */
    fun fetchMovimientosAhorro(numeroCuenta: Int, cantidad: Int = 20) {
        val rut = sessionManager.getUserRut().toString()
        val accessToken = sessionManager.getAccessToken().toString()
        if (rut.isBlank()) {
            _error.value = "RUT no disponible"
            Log.e("MovimientosAhorroViewModel", "RUT no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("MovimientosAhorroViewModel", "Iniciando obtención de movimientos para cuenta: $numeroCuenta, RUT: $rut")

            when (val result = repository.getMovimientosAhorro(numeroCuenta, cantidad, accessToken)) {
                is Result.Success -> {
                    // Asignar los movimientos obtenidos
                    _movimientosData.value = ApiResponse<MovimientosAhorro>(
                        count = result.data.count,
                        data = result.data.data,
                        error_code = result.data.error_code,
                    )
                    _error.value = null
                    Log.d("MovimientosAhorroViewModel", "Movimientos obtenidos: ${result.data} movimientos")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("MovimientosAhorroViewModel", "Error al obtener movimientos: ${result.exception.message}")
                }
                else -> {
                    Log.d("MovimientosAhorroViewModel", "Estado inesperado")
                }
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