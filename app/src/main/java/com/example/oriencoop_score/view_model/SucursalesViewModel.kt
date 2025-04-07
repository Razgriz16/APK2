package com.example.oriencoop_score.view_model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.RegionViewData
import com.example.oriencoop_score.repository.SucursalesRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.model.ComunaViewData
import com.example.oriencoop_score.model.Comunas
import com.example.oriencoop_score.model.SucursalViewData
import com.example.oriencoop_score.model.Sucursales
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SucursalesViewModel @Inject constructor(
    private val sucursalesRepository: SucursalesRepository
) : ViewModel() {

    companion object {
        private const val TAG = "SucursalesViewModel"
        private const val ACTIVE_FLAG = "S"
        // Valores predeterminados para mostrar en lugar de null
        private const val DEFAULT_STRING_VALUE = "No disponible"
        private const val DEFAULT_REGION_NAME = "Región no especificada"
        private const val DEFAULT_COMUNA_NAME = "Comuna no especificada"
        private const val DEFAULT_SUCURSAL_NAME = "Sucursal no especificada"
    }

    private val _groupedSucursalesState =
        MutableStateFlow<Result<List<RegionViewData>>>(Result.Loading)
    val groupedSucursalesState: StateFlow<Result<List<RegionViewData>>> =
        _groupedSucursalesState.asStateFlow()

    // --- loadAndGroupSucursales (sin cambios en la estructura principal) ---
    // La anotación @RequiresApi sigue siendo necesaria por computeIfAbsent
    @RequiresApi(Build.VERSION_CODES.N)
    fun loadAndGroupSucursales() {
        Log.d(TAG, "loadAndGroupSucursales: Iniciando carga y agrupación.")
        _groupedSucursalesState.value = Result.Loading
        viewModelScope.launch {
            try {
                Log.d(TAG, "loadAndGroupSucursales: Obteniendo datos del repositorio...")
                val comunasResult = sucursalesRepository.getComunas()
                val sucursalesResult = sucursalesRepository.getSucursales()

                if (comunasResult is Result.Success && sucursalesResult is Result.Success) {
                    Log.d(TAG, "loadAndGroupSucursales: Datos obtenidos exitosamente.")
                    val allComunas = comunasResult.data
                    val allSucursales = sucursalesResult.data

                    Log.d(TAG, "loadAndGroupSucursales: Iniciando filtrado y agrupación...")
                    val groupedData = groupAndFilterData(allComunas, allSucursales) // Llamada a la función modificada
                    Log.d(TAG, "loadAndGroupSucursales: Filtrado y agrupación completados. Regiones encontradas: ${groupedData.size}")

                    //printGroupedData(groupedData) // Llamada a función para imprimir datos agrupados
                    _groupedSucursalesState.value = Result.Success(groupedData)

                } else {
                    val errorException = when {
                        comunasResult is Result.Error -> comunasResult.exception
                        sucursalesResult is Result.Error -> sucursalesResult.exception
                        else -> IOException("Error desconocido al obtener datos del repositorio")
                    }
                    Log.e(TAG, "loadAndGroupSucursales: Error al obtener datos del repositorio.", errorException)
                    _groupedSucursalesState.value = Result.Error(errorException)
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadAndGroupSucursales: Excepción durante el proceso general.", e)
                _groupedSucursalesState.value = Result.Error(e)
            }
        }
    }

    /**
     * Filtra, agrupa y maneja valores nulos.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun groupAndFilterData(
        comunas: List<Comunas>,
        sucursales: List<Sucursales>
    ): List<RegionViewData> {

        // Filtrar sucursales activas (estaactiva sigue siendo no nulo)
        val activeSucursales = sucursales.filter {
            // No se necesita cambio aquí si estaactiva sigue siendo String no null
            it.estaactiva.equals(ACTIVE_FLAG, ignoreCase = true)
        }
        Log.d(TAG, "groupAndFilterData: Sucursales activas encontradas: ${activeSucursales.size}")

        // Filtrar comunas activas (¡manejar activa nullable!) y crear mapa
        val activeComunasMap = comunas
            // *** CAMBIO CLAVE: Filtro seguro para comuna.activa nullable ***
            .filter { ACTIVE_FLAG.equals(it.activa, ignoreCase = true) } // Esta forma maneja it.activa == null de forma segura
            .associateBy { it.codigocomuna }
        Log.d(TAG, "groupAndFilterData: Comunas activas encontradas: ${activeComunasMap.size}")

        // Agrupar sucursales activas por código de comuna (sin cambios necesarios aquí)
        val activeSucursalesByComunaCode = activeSucursales.groupBy { it.codigocomuna }

        val regionMap = mutableMapOf<String, MutableList<ComunaViewData>>()

        activeComunasMap.values.forEach { comuna ->
            val sucursalesInThisComuna = activeSucursalesByComunaCode[comuna.codigocomuna]

            if (!sucursalesInThisComuna.isNullOrEmpty()) {
                // *** CAMBIO CLAVE: Mapeo con valores predeterminados para SucursalViewData ***
                val sucursalViewDataList = sucursalesInThisComuna.map { suc ->
                    SucursalViewData(
                        // Usa el operador Elvis (?:) para dar un valor por defecto si es null
                        nombre = suc.nombre ?: DEFAULT_SUCURSAL_NAME,
                        direccion = suc.direccion ?: DEFAULT_STRING_VALUE,
                        horario = suc.horario ?: DEFAULT_STRING_VALUE,
                        telefono = suc.telefono, // Se mantiene null si es null
                        correo = suc.correo    // Se mantiene null si es null
                    )
                }
                    // *** CAMBIO CLAVE: Ordenamiento seguro por nombre (usando el valor por defecto) ***
                    .sortedBy { it.nombre ?: "" } // Ordena usando el nombre o "" si es null

                // *** CAMBIO CLAVE: Mapeo con valor predeterminado para ComunaViewData ***
                val comunaViewData = ComunaViewData(
                    nombreComuna = comuna.nombre ?: DEFAULT_COMUNA_NAME,
                    sucursales = sucursalViewDataList
                )

                // Añadir al mapa de regiones (comuna.region sigue siendo no nulo)
                // Si comuna.region pudiera ser null, necesitaríamos: (comuna.region ?: DEFAULT_REGION_NAME)
                regionMap.computeIfAbsent(comuna.region) { mutableListOf() }
                    .add(comunaViewData)
                // El log usa el nombre original o el default si era null
                Log.d(TAG, "groupAndFilterData: Añadida Comuna activa '${comunaViewData.nombreComuna}' con ${sucursalViewDataList.size} sucursales a Región '${comuna.region}'")

            } else {
                // El log usa el nombre original o el default si era null
                Log.v(TAG, "groupAndFilterData: Comuna activa '${comuna.nombre ?: DEFAULT_COMUNA_NAME}' no tiene sucursales activas asociadas. Omitiendo.")
            }
        }

        return regionMap.map { (regionName, comunaList) ->
            RegionViewData(
                nombreRegion = regionName, // Asumiendo que regionName no es null
                // *** CAMBIO CLAVE: Ordenamiento seguro por nombre de comuna ***
                comunas = comunaList.sortedBy { it.nombreComuna ?: "" } // Ordena usando nombre o "" si es null
            )
        }
            // *** CAMBIO CLAVE: Ordenamiento seguro por nombre de región ***
            .sortedBy { it.nombreRegion ?: "" } // Ordena usando nombre o "" si es null (aunque asumimos no nulo)
    }


    /**
     * Función temporal para imprimir la estructura de datos agrupada en la consola,
     * manejando valores nulos en ViewData.
     * @param groupedData La lista de [RegionViewData] a imprimir.
     */
    private fun printGroupedData(groupedData: List<RegionViewData>) {
        println("--- INICIO DATOS AGRUPADOS ---")
        if (groupedData.isEmpty()) {
            println("No se encontraron regiones con comunas y sucursales activas.")
        } else {
            groupedData.forEach { region ->
                println("=======================================")
                // *** CAMBIO: Manejo de null para nombreRegion (aunque se asume no nulo) ***
                println("REGIÓN: ${region.nombreRegion ?: DEFAULT_REGION_NAME}")
                println("=======================================")
                if (region.comunas.isEmpty()) {
                    println("  (No hay comunas activas con sucursales activas en esta región)")
                } else {
                    region.comunas.forEach { comuna ->
                        // *** CAMBIO: Manejo de null para nombreComuna ***
                        println("  * COMUNA: ${comuna.nombreComuna ?: DEFAULT_COMUNA_NAME}")
                        if (comuna.sucursales.isEmpty()) {
                            println("      (No hay sucursales activas en esta comuna)")
                        } else {
                            comuna.sucursales.forEach { sucursal ->
                                // *** CAMBIOS: Manejo de null para campos de sucursal ***
                                println("      - Sucursal: ${sucursal.nombre ?: DEFAULT_SUCURSAL_NAME}")
                                println("        > Dirección: ${sucursal.direccion ?: DEFAULT_STRING_VALUE}")
                                println("        > Horario: ${sucursal.horario ?: DEFAULT_STRING_VALUE}")
                                println("        > Teléfono: ${sucursal.telefono ?: "N/A"}") // Puedes usar "N/A" u otro
                                println("        > Correo: ${sucursal.correo ?: "N/A"}")
                            }
                        }
                        println("    -----------------------------------")
                    }
                }
            }
        }
        println("--- FIN DATOS AGRUPADOS ---")
    }
}