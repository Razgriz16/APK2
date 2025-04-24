package com.example.oriencoop_score.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.repository.CreditoCuotasRepository
import com.example.oriencoop_score.repository.CuentaAhorroRepository
import com.example.oriencoop_score.utility.ProductoRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MisProductosViewModel @Inject constructor(
    private val creditoCuotasRepository: CreditoCuotasRepository,
    private val cuentaAhorroRepository: CuentaAhorroRepository,
    // Inyecta aquí los repositorios de otros productos, ej: private val ahorroRepository: AhorroRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Mapa que indica qué productos están activos (true = mostrar, false = no mostrar)
    private val _productosActivos = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val productosActivos: StateFlow<Map<String, Boolean>> = _productosActivos

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        obtenerEstadoProductos()
    }

    fun obtenerEstadoProductos() {
        val token = sessionManager.getAccessToken().toString()
        val rut = sessionManager.getUserRut().toString()

        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            try {
                // Realizar consultas concurrentes a los repositorios
                val resultados = listOf(
                    async { verificarProducto(creditoCuotasRepository, token, rut, "CREDITO") },
                    async { verificarProducto(cuentaAhorroRepository, token, rut, "AHORRO") }
                    // Agrega aquí las consultas para otros productos, ej:
                    // async { verificarProducto(ahorroRepository, token, rut, "AHORRO") },
                ).awaitAll()

                // Convertir los resultados en un mapa
                val mapaProductos = resultados.associateBy({ it.first }, { it.second })
                _productosActivos.value = mapaProductos
                _error.value = null // Limpiar errores previos
            } catch (e: Exception) {
                _error.value = e.message // Guardar el mensaje de error
                _productosActivos.value = emptyMap() // Limpiar productos en caso de error
            } finally {
                _isLoading.value = false // Finalizar la carga
            }
        }
    }

    // Función auxiliar para verificar el estado de un producto
    private suspend fun <T> verificarProducto(
        repository: ProductoRepository<T>,
        token: String,
        rut: String,
        nombreProducto: String
    ): Pair<String, Boolean> {
        return try {
            when (val result = repository.fetchProducto(rut)) {
                is Result.Success -> {
                    val count = result.data.count
                    nombreProducto to (count > 0) // true si count > 0, false si count == 0
                }
                is Result.Error -> {
                    nombreProducto to false // En caso de error, asumimos que no está activo
                }
                else -> nombreProducto to false
            }
        } catch (e: Exception) {
            nombreProducto to false // En caso de excepción, asumimos que no está activo
        }
    }
}