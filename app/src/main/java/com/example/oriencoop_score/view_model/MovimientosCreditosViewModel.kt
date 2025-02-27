package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.repository.MovimientosCreditosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosCreditosViewModel @Inject constructor(
    private val repository: MovimientosCreditosRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _movimientos = MutableStateFlow<List<MovimientosCreditos>>(emptyList())
    val movimientos: StateFlow<List<MovimientosCreditos>> = _movimientos

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        obtenerMovimientosCreditos()
    }
    fun cargarMovimientos(nroCuenta: Long) {
        viewModelScope.launch {
            //Reseteamos estados
            _isLoading.value = true
            _error.value = null
            _movimientos.value = emptyList()
            try {
                //Simulamos la carga de datos. Sustituir por la llamada real al repositorio/api
                val movimientosDeCuenta = movimientos.value.get(nroCuenta.toInt()) // Implementa esta función
                _movimientos.value = listOf(movimientosDeCuenta)
                _isLoading.value = false // Éxito
            } catch (e: Exception) {
                _error.value = "Error al cargar movimientos: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun obtenerMovimientosCreditos() {
        val token = sessionManager.token.value
        val rut = sessionManager.username.value
        if (token.isBlank() || rut.isBlank()){ //Comprueba si son blancos
            Log.e("MovimientosCreditosViewModel", "Token o Rut no pueden estar vacíos")
            _error.value = "Token o Rut no pueden estar vacíos"
            return
        }
        viewModelScope.launch {
            Log.d("MovimientosCreditosViewModel", "Llamando función obtenerMovimientos")
            _isLoading.value = true // Indicar que se está cargando
            when (val result = repository.getMovimientosCreditos(token, rut)) {
                is Result.Success -> {
                    Log.d("MovimientosCreditosViewModel", "Llamada exitosa. BODY: "+result.data)
                    _movimientos.value = result.data.movimientos_creditos
                    _error.value = null
                }
                is Result.Error -> {
                    Log.e("MovimientosCreditosViewModel", "Llamada fallida. Error: ${result.exception.message}")
                    _error.value = result.exception.message // Guardar el mensaje de error
                    _movimientos.value = emptyList() // Limpiar los productos en caso de error
                }
                Result.Loading -> Result.Loading
            }
            _isLoading.value = false // Finalizar la carga
        }
    }
}