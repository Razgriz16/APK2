package com.example.oriencoop_score.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import com.example.oriencoop_score.model.ClienteInfoResponse
import com.example.oriencoop_score.repository.ClienteInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteInfoViewModel @Inject constructor(
    private val repository: ClienteInfoRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _clienteInfo = MutableStateFlow<ClienteInfoResponse?>(null)
    val clienteInfo: StateFlow<ClienteInfoResponse?> = _clienteInfo

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        obtenerClienteInfo()
    }

    
    fun obtenerClienteInfo(){
        val token = sessionManager.token.value ?:""
        val rut = sessionManager.username.value ?:""
        if (token.isBlank() || rut.isBlank()) {
            Log.e("ClienteInfoRepository", "Token o Rut no pueden estar vacíos")
            _error.value = "Token o Rut no pueden estar vacíos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("ClienteInfoRepository", "Token: $token, Rut: $rut")

            try {
                when (val result = repository.getClienteInfo(token, rut)) {
                    is Result.Success -> {
                        Log.d("ClienteInfoRepository", "Datos obtenidos: ${result.data}")
                        _clienteInfo.value = result.data
                    }
                    is Result.Error -> {
                        Log.e("ClienteInfoRepository", "Error al obtener datos: ${result.exception.message}")
                        _error.value = result.exception.message
                    }
                    Result.Loading -> Result.Loading
                }
            } catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                Log.e("ClienteInfoRepository", "Exception thrown: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}