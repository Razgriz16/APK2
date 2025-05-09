package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.model.ApiResponse
import com.example.oriencoop_score.model.Lcr
import com.example.oriencoop_score.repository.LcrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LcrViewModel @Inject constructor(
    private val repository: LcrRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _lcrData = MutableStateFlow<ApiResponse<Lcr>?>(null)
    val lcrData: StateFlow<ApiResponse<Lcr>?> = _lcrData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchLcrData()
    }

    fun fetchLcrData() {
        val rut = sessionManager.getUserRut().toString()
        val accessToken = sessionManager.getAccessToken().toString()
        if (rut.isBlank()) {
            _error.value = "RUT no disponible"
            Log.e("LcrViewModel", "Cédula no disponible")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("LcrViewModel", "Iniciando obtención de datos para cédula: $rut")

            when (val result = repository.fetchProducto(rut, accessToken)) {
                is Result.Success -> {
                    _lcrData.value = result.data
                    _error.value = null
                    Log.d("LcrViewModel", "Datos obtenidos: ${result.data.data.size} registros LCR")
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e("LcrViewModel", "Error al obtener datos: ${result.exception.message}")
                }
                Result.Loading -> Result.Loading
            }
            _isLoading.value = false
        }
    }
}