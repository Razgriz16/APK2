package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.Lcc
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.repository.LccRepository
import com.example.oriencoop_score.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LccViewModel @Inject constructor(
    private val repository: LccRepository,
    private val sessionManager: SessionManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TAG = "LccViewModel"
        const val NRO_CUENTA_KEY = "nroCuenta"
    }

    // Estado para los datos LCC
    private val _lccData = MutableStateFlow<ApiResponse<Lcc>?>(null)
    val lccData: StateFlow<ApiResponse<Lcc>?> = _lccData

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        Log.d(TAG, "LccViewModel initialized!")
        fetchLccData()
    }

    /**
     * Obtiene los datos LCC para el usuario autenticado.
     * Valida el RUT y ejecuta la llamada al repositorio.
     */
    fun fetchLccData() {
        val rut = sessionManager.getUserRut().toString()
        val accessToken = sessionManager.getAccessToken().toString()
        if (rut.isBlank()) {
            _error.value = "El RUT no puede estar vacío"
            Log.e(TAG, "El RUT no puede estar vacío")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d(TAG, "Iniciando obtención de datos LCC para RUT: $rut")

            when (val result = repository.fetchProducto(rut, accessToken)) {
                is Result.Success -> {
                    _lccData.value = result.data
                    val nroCuenta = result.data.data.firstOrNull()?.codigo
                    if (nroCuenta != null) {
                        sessionManager.saveNroCuenta(nroCuenta)
                        savedStateHandle[NRO_CUENTA_KEY] = nroCuenta
                        Log.d(TAG, "Número de cuenta guardado: $nroCuenta")
                    }
                    Log.d(TAG, "Datos LCC obtenidos exitosamente: ${result.data}")
                    _error.value = null
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Error desconocido"
                    Log.e(TAG, "Error al obtener datos LCC: ${result.exception.message}")
                }

                Result.Loading -> TODO()
            }
            _isLoading.value = false
        }
    }

    /**
     * Limpia el estado de error.
     */
    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "LccViewModel cleared")
    }
}