package com.example.oriencoop_score.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.MisProductosResponse
import com.example.oriencoop_score.repository.MisProductosRepository
import kotlinx.coroutines.launch
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MisProductosViewModel @Inject constructor(
    private val repository: MisProductosRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado inicial para los productos
    //private val _productos = MutableStateFlow<MisProductosResponse?>(null)
    //val productos: StateFlow<MisProductosResponse?> = _productos

    private val _productos = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val productos: StateFlow<Map<String, Boolean>> = _productos

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        obtenerProductos()
    }

    fun obtenerProductos() {
        val token = sessionManager.token.value
        val rut = sessionManager.username.value
        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            try {
                when (val result = repository.getProductos(token, rut)) {
                    is Result.Success -> {
                        _productos.value = convertResponseToMap(result.data)
                        _error.value = null // Limpiar errores previos
                    }

                    is Result.Error -> {
                        _error.value = result.exception.message // Guardar el mensaje de error
                        _productos.value = emptyMap() // Limpiar los productos en caso de error
                    }

                    Result.Loading -> Result.Loading
                }
            }
            catch (e: Exception) {
                // Catch any exceptions thrown by the repository and set the error state
                _error.value = e.message
                _productos.value = emptyMap() // Limpiar los productos en caso de error
            }finally {
                _isLoading.value = false // Finalizar la carga
            }
        }
    }
    private fun convertResponseToMap(response: MisProductosResponse): Map<String, Boolean> {
        return mapOf(
            "CREDITO" to (response.CREDITO == 1),
            "AHORRO" to (response.AHORRO == 1),
            "DEPOSTO" to (response.DEPOSTO == 1),
            "LCC" to (response.LCC == 1),
            "LCR" to (response.LCR == 1),
            "CSOCIAL" to (response.CSOCIAL == 1)
        )
    }
}
