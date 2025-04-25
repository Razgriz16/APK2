package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.repository.CuentaAhorroRepository
import com.example.oriencoop_score.utility.ApiResponse
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CuentaAhorroViewModel @Inject constructor(
    private val repository: CuentaAhorroRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado para los datos de las cuentas de ahorro
    private val _cuentaAhorroData = MutableStateFlow<ApiResponse<CuentaAhorro>?>(null)
    val cuentaAhorroData: StateFlow<ApiResponse<CuentaAhorro>?> = _cuentaAhorroData.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Cuenta seleccionada para expandir detalles
    private val _cuentaSeleccionada = MutableStateFlow<CuentaAhorro?>(null)
    val cuentaSeleccionada: StateFlow<CuentaAhorro?> = _cuentaSeleccionada.asStateFlow()

    init {
        fetchCuentaAhorro()
    }

    /**
     * Obtiene los datos de las cuentas de ahorro desde el repositorio.
     * Valida la cédula del usuario desde el sessionManager antes de realizar la llamada.
     */
    fun fetchCuentaAhorro() {
        val rut = sessionManager.getUserRut().toString()
        if (rut.isBlank()) {
            _error.value = "Cédula no disponible"
            Log.e("CuentaAhorroViewModel", "Cédula no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("CuentaAhorroViewModel", "Iniciando obtención de datos para cédula: $rut")

            when (val result = repository.fetchProducto(rut)) {
                is Result.Success -> {
                    // Mapear las cuentas para incluir el número de cuenta formateado
                    val formattedCuentas = result.data.copy(
                        data = result.data.data.map { cuenta ->
                            cuenta.copy() // Creamos una copia para mantener inmutabilidad
                        }
                    )
                    _cuentaAhorroData.value = formattedCuentas
                    _error.value = null
                    Log.d("CuentaAhorroViewModel", "Datos obtenidos: ${formattedCuentas.data.size} cuentas")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("CuentaAhorroViewModel", "Error al obtener datos: ${result.exception.message}")
                }
                else -> {
                    Log.d("CuentaAhorroViewModel", "Estado no manejado")
                }
            }
            _isLoading.value = false
        }
    }

    /**
     * Formatea el número de cuenta según el patrón: 00codigosistema + 000codigosucursal + 0000000numerocuenta + 0digitocuenta
     *
     * @param cuenta La cuenta de ahorro.
     * @return El número de cuenta formateado como String de 13 dígitos.
     */
    private fun formatNumeroCuenta(cuenta: CuentaAhorro): String {
        return String.format(Locale.ROOT,
            "%02d%03d%07d%01d",
            cuenta.codigoSistema,
            cuenta.codigoSucursal,
            cuenta.numeroCuenta,
            cuenta.digitoCuenta
        )
    }

    /**
     * Selecciona una cuenta para mostrar sus detalles.
     * Si se selecciona la misma cuenta, se deselecciona (toggle).
     *
     * @param cuenta La cuenta seleccionada.
     */
    fun selectCuenta(cuenta: CuentaAhorro) {
        Log.d("CuentaAhorroViewModel", "Seleccionando cuenta: ${cuenta.numeroCuenta}")
        if (_cuentaSeleccionada.value?.numeroCuenta == cuenta.numeroCuenta) {
            _cuentaSeleccionada.value = null
        } else {
            _cuentaSeleccionada.value = cuenta
        }
    }

    /**
     * Limpia la cuenta seleccionada.
     */
    fun clearCuentaSeleccionada() {
        Log.d("CuentaAhorroViewModel", "Deseleccionando cuenta")
        _cuentaSeleccionada.value = null
    }
}