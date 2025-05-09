package com.example.oriencoop_score.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.repository.CreditoCuotasRepository
import com.example.oriencoop_score.repository.CuentaAhorroRepository
import com.example.oriencoop_score.repository.LccRepository
import com.example.oriencoop_score.model.ProductoRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.auth.SessionManager
import com.example.oriencoop_score.repository.DapRepository
import com.example.oriencoop_score.repository.LcrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Realiza consultas concurrentes a los repositorios de productos específicos para obtener el estado de los productos
 * asociados a un usuario.
 *
 * Es decir, realiza múltiples llamadas a cada producto para verificar si el usuario tiene los productos activos
 * y actualiza el estado de los productos en el ViewModel para mostrarlos en el menú "Mis Productos".
 * Ejecuta la llamada de red en el hilo de IO y maneja la respuesta específica.
 *
 * @param rut RUT del usuario para saber los productos que tiene.
 * @param token Token de autenticación para la llamada API.
 * @return Un [Result] que contiene [Result.Success] con [ApiResponse<MovimientosCsocial>]
 *         o [Result.Error] con la [Throwable] correspondiente.
 */
@HiltViewModel
class MisProductosViewModel @Inject constructor(
    private val creditoCuotasRepository: CreditoCuotasRepository,
    private val cuentaAhorroRepository: CuentaAhorroRepository,
    private val lccRepository: LccRepository,
    private val dapRepository: DapRepository,
    private val lcrRepository: LcrRepository,
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
        val accessToken = sessionManager.getAccessToken().toString()
        val rut = sessionManager.getUserRut().toString()

        viewModelScope.launch {
            _isLoading.value = true // Indicar que se está cargando
            try {
                // Realizar consultas concurrentes a los repositorios
                val resultados = listOf(
                    async { verificarProducto(creditoCuotasRepository, accessToken, rut, "CREDITO") },
                    async { verificarProducto(cuentaAhorroRepository, accessToken, rut, "AHORRO") },
                    async { verificarProducto(lccRepository, accessToken, rut, "LCC") },
                    async { verificarProducto(dapRepository, accessToken, rut, "DAP") },
                    async { verificarProducto(lcrRepository, accessToken, rut, "LCR") },

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
            when (val result = repository.fetchProducto(rut, token)) {
                is Result.Success -> {
                    val count = result.data.count
                    val activo = result.data.data.size > 0
                    nombreProducto to (count > 0 || activo == true) // true si count > 0, false si count == 0
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