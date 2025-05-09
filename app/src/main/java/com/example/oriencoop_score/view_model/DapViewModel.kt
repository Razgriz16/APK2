package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.Dap
import com.example.oriencoop_score.repository.DapRepository
import com.example.oriencoop_score.utility.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DapViewModel @Inject constructor(
    private val repository: DapRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado para los datos de DAP
    private val _dapData = MutableStateFlow<ApiResponse<Dap>?>(null)
    val dapData: StateFlow<ApiResponse<Dap>?> = _dapData.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // DAP seleccionado para expandir detalles
    private val _cuentaSeleccionada = MutableStateFlow<Dap?>(null)
    val cuentaSeleccionada: StateFlow<Dap?> = _cuentaSeleccionada.asStateFlow()

    init {
        fetchDap()
    }

    /**
     * Obtiene los datos de DAP desde el repositorio.
     * Valida la cédula del usuario desde el sessionManager antes de realizar la llamada.
     */
    fun fetchDap() {
        val rut = sessionManager.getUserRut().toString()
        val accessToken = sessionManager.getAccessToken().toString()
        if (rut.isBlank()) {
            _error.value = "Cédula no disponible"
            Log.e("DapViewModel", "Cédula no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("DapViewModel", "Iniciando obtención de datos para cédula: $rut")

            when (val result = repository.fetchProducto(rut, accessToken)) {
                is Result.Success -> {
                    // Mapear los datos para mantener inmutabilidad
                    val formattedDap = result.data.copy()
                    _dapData.value = formattedDap
                    _error.value = null
                    Log.d("DapViewModel", "Datos obtenidos: DAP recibido")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("DapViewModel", "Error al obtener datos: ${result.exception.message}")
                }
                else -> {
                    Log.d("DapViewModel", "Estado no manejado")
                }
            }
            _isLoading.value = false
        }
    }

    /**
     * Selecciona un DAP para mostrar sus detalles.
     * Si se selecciona el mismo DAP, se deselecciona (toggle).
     *
     * @param dap El DAP seleccionado.
     */
    fun selectDap(dap: Dap) {
        Log.d("DapViewModel", "Seleccionando DAP: ${dap.numeroDeposito}")
        if (_cuentaSeleccionada.value?.numeroDeposito == dap.numeroDeposito) {
            _cuentaSeleccionada.value = null
        } else {
            _cuentaSeleccionada.value = dap
        }
    }

    /**
     * Limpia el DAP seleccionado.
     */
    fun clearDapSeleccionado() {
        Log.d("DapViewModel", "Deseleccionando DAP")
        _cuentaSeleccionada.value = null
    }
}