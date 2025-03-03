package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.MovimientosLcr
import com.example.oriencoop_score.repository.MovimientosLcrRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientosLcrViewModel @Inject constructor(
    private val repository: MovimientosLcrRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    // Estado inicial para los productos
    //private val _productos = MutableStateFlow<MisProductosResponse?>(null)
    //val productos: StateFlow<MisProductosResponse?> = _productos

    private val _movimientoslcr = MutableStateFlow<List<MovimientosLcr>>(emptyList())
    val movimientoslcr: StateFlow<List<MovimientosLcr>> = _movimientoslcr

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        obtenerMovimientosLcr()
    }

    fun obtenerMovimientosLcr() {
        val token = sessionManager.token.value
        val rut = sessionManager.username.value
        if (token.isBlank() || rut.isBlank()){ //Comprueba si son blancos
            Log.e("MovimientosLcrViewModel", "Token o Rut no pueden estar vacíos")
            _error.value = "Token o Rut no pueden estar vacíos"
            return
        }
        viewModelScope.launch {
            Log.d("MovimientosLcrViewModel", "Llamando función obtenerMovimientos")
            _isLoading.value = true // Indicar que se está cargando
            when (val result = repository.getMovimientosLcr(token, rut)) {
                is Result.Success -> {
                    Log.d("MovimientosLcrViewModel", "Llamada exitosa. BODY: "+result.data)
                    _movimientoslcr.value = result.data.movimientos_lcr
                    _error.value = null
                }
                is Result.Error -> {
                    Log.e("MovimientosLcrViewModel", "Llamada fallida. Error: ${result.exception.message}")
                    _error.value = result.exception.message // Guardar el mensaje de error
                    _movimientoslcr.value = emptyList() // Limpiar los productos en caso de error
                }
                Result.Loading -> _isLoading.value=false
            }
            _isLoading.value = false // Finalizar la carga
        }
    }
}