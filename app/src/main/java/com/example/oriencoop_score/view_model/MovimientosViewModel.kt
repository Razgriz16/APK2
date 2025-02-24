package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.model.MisProductosResponse
import com.example.oriencoop_score.model.Movimiento
import com.example.oriencoop_score.model.MovimientosResponse
import com.example.oriencoop_score.repository.MovimientosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosViewModel @Inject constructor(
    private val repository: MovimientosRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    // Estado inicial para los productos
    //private val _productos = MutableStateFlow<MisProductosResponse?>(null)
    //val productos: StateFlow<MisProductosResponse?> = _productos

    private val _movimientos = MutableStateFlow<List<Movimiento>>(emptyList())
    val movimientos: StateFlow<List<Movimiento>> = _movimientos

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        obtenerMovimientos()
    }

    fun obtenerMovimientos() {
        val token = sessionManager.token.value
        val rut = sessionManager.username.value
        if (token.isBlank() || rut.isBlank()){ //Comprueba si son blancos
            Log.e("MovimientosViewModel", "Token o Rut no pueden estar vacíos")
            _error.value = "Token o Rut no pueden estar vacíos"
            return
        }
        viewModelScope.launch {
            Log.d("MovimientosViewModel", "Llamando función obtenerMovimientos")
            _isLoading.value = true // Indicar que se está cargando
            when (val result = repository.getMovimientos(token, rut)) {
                is Result.Success -> {
                    Log.d("MovimientosViewModel", "Llamada exitosa. BODY: "+result.data)
                    _movimientos.value = result.data.movimientos
                    _error.value = null
                }
                is Result.Error -> {
                    Log.e("MovimientosViewModel", "Llamada fallida. Error: ${result.exception.message}")
                    _error.value = result.exception.message // Guardar el mensaje de error
                    _movimientos.value = emptyList() // Limpiar los productos en caso de error
                }
                Result.Loading -> Result.Loading
            }
            _isLoading.value = false // Finalizar la carga
        }
    }
}
