package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.repository.MovimientosCreditosRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosCreditosViewModel @Inject constructor(
    private val repository: MovimientosCreditosRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado para los datos de movimientos de créditos
    private val _movimientosData = MutableStateFlow<ApiResponse<MovimientosCreditos>?>(null)
    val movimientosData: StateFlow<ApiResponse<MovimientosCreditos>?> = _movimientosData.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Carga los movimientos de créditos para un número de cuenta específico.
     *
     * @param numeroCuenta El número de cuenta (crédito) para obtener los movimientos.
     */
    fun fetchMovimientosCreditos(numeroCuenta: Long) {
        val rut = sessionManager.getUserRut().toString()
        if (rut.isBlank()) {
            _error.value = "RUT no disponible"
            Log.e("MovimientosCreditosViewModel", "RUT no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("MovimientosCreditosViewModel", "Iniciando obtención de movimientos para cuenta: $numeroCuenta, RUT: $rut")

            when (val result = repository.getMovimientosCreditos(numeroCuenta)) {
                is Result.Success -> {
                    // Filtrar movimientos por número de cuenta
                    val movimientosFiltrados = result.data.data
                    _movimientosData.value = ApiResponse<MovimientosCreditos>(
                        count = movimientosFiltrados.size,
                        data = movimientosFiltrados,
                        error_code = result.data.error_code,
                    )
                    _error.value = null
                    Log.d("MovimientosCreditosViewModel", "Movimientos obtenidos: ${movimientosFiltrados.size} movimientos")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("MovimientosCreditosViewModel", "Error al obtener movimientos: ${result.exception.message}")
                }
                else -> {
                    Log.d("MovimientosCreditosViewModel", "Estado inesperado")
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