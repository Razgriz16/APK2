package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.ApiResponse
import com.example.oriencoop_score.model.CreditoCuotas
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.repository.CreditoCuotasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreditoCuotasViewModel @Inject constructor(
    private val repository: CreditoCuotasRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado para los datos de las cuotas de crédito
    private val _creditoCuotasData = MutableStateFlow<ApiResponse<CreditoCuotas>?>(null)
    val creditoCuotasData: StateFlow<ApiResponse<CreditoCuotas>?> = _creditoCuotasData.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Cuota seleccionada para expandir detalles
    private val _cuentaSeleccionada = MutableStateFlow<CreditoCuotas?>(null)
    val cuentaSeleccionada: StateFlow<CreditoCuotas?> = _cuentaSeleccionada.asStateFlow()

    init {
        fetchCreditoCuotas()
    }

    /**
     * Obtiene los datos de las cuotas de crédito desde el repositorio.
     * Valida la cédula del usuario desde el sessionManager antes de realizar la llamada.
     */
    fun fetchCreditoCuotas() {
        val cedula = sessionManager.getUserRut().toString()
        if (cedula.isBlank()) {
            _error.value = "Cédula no disponible"
            Log.e("CreditoCuotasViewModel", "Cédula no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("CreditoCuotasViewModel", "Iniciando obtención de datos para cédula: $cedula")

            when (val result = repository.fetchProducto(cedula)) {
                is Result.Success -> {
                    _creditoCuotasData.value = result.data
                    _error.value = null
                    Log.d("CreditoCuotasViewModel", "Datos obtenidos: ${result.data.data.size} cuentas")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("CreditoCuotasViewModel", "Error al obtener datos: ${result.exception.message}")
                }

                Result.Loading -> Result.Loading
            }
            _isLoading.value = false
        }
    }

    /**
     * Selecciona una cuota para mostrar sus detalles.
     * Si se selecciona la misma cuota, se deselecciona (toggle).
     *
     * @param cuota La cuota seleccionada.
     */
    fun selectCuota(cuota: CreditoCuotas) {
        Log.d("CreditoCuotasViewModel", "Seleccionando cuota: ${cuota.credito}")
        if (_cuentaSeleccionada.value?.credito != cuota.credito) {
            _cuentaSeleccionada.value = cuota
        }
    }

    fun clearSelection() {
        Log.d("CreditoCuotasViewModel", "Deseleccionando cuota")
        _cuentaSeleccionada.value = null
    }

    /**
     * Limpia la cuota seleccionada.
     */
    fun clearCuotaSeleccionada() {
        _cuentaSeleccionada.value = null
    }
}
 