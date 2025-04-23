package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.FacturasLcc
import com.example.oriencoop_score.model.MovimientosLcc
import com.example.oriencoop_score.repository.FacturasLccRepository
import com.example.oriencoop_score.repository.MovimientosLccRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FacturasLccViewModel @Inject constructor(
    private val repository: FacturasLccRepository,
    private val sessionManager: SessionManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _facturaslcc = MutableStateFlow<List<FacturasLcc>>(emptyList())
    val facturaslcc: StateFlow<List<FacturasLcc>> = _facturaslcc

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        Log.d("FacturasLccViewModel", "FacturasLccViewModel initialized!")

        // Observe nroCuenta changes and load data when it becomes available
        viewModelScope.launch {
            val cuenta = sessionManager.getNroCuenta()
            Log.d("FacturasLccViewModel", "nroCuenta obtained: $cuenta")
            if (cuenta != 0L) {
                obtenerFacturasLcc(cuenta)
            }
        }
    }

    fun obtenerFacturasLcc(cuenta: Long = sessionManager.getNroCuenta()) {
        val token = sessionManager.getAccessToken().toString() ?: ""
        Log.d("FacturasLccViewModel", "numero cuenta: $cuenta")

        if (cuenta == 0L) {
            Log.d("FacturasLccViewModel", "Cuenta inválida (0), no se hará la petición")
            return
        }

        viewModelScope.launch {
            Log.d("FacturasLccViewModel", "Llamando función obtenerFacturasLcc")
            _isLoading.value = true // Indicar que se está cargando

            when (val result = repository.getFacturasLcc(token, cuenta)) {
                is Result.Success -> {
                    Log.d("FacturasLccViewModel", "Llamada exitosa. BODY: "+result.data)
                    _facturaslcc.value = result.data.facturas_lcc
                    _error.value = null
                }
                is Result.Error -> {
                    Log.e("FacturasLccViewModel", "Llamada fallida. Error: ${result.exception.message}")
                    _error.value = result.exception.message
                    _facturaslcc.value = emptyList()
                }
                Result.Loading -> _isLoading.value = true
            }
            _isLoading.value = false
        }
    }
}