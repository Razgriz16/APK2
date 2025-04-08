package com.example.oriencoop_score.view.pantalla_principal.Sucursales


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.oriencoop_score.model.ComunaViewData
import com.example.oriencoop_score.model.RegionViewData
import com.example.oriencoop_score.model.SucursalViewData
import com.example.oriencoop_score.view_model.SucursalesViewModel
import com.example.oriencoop_score.utility.Result

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Icono para atrás
import androidx.compose.material3.* // O androidx.compose.material.*
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items // Usar items para listas dinámicas
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown // Icono para desplegar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar

// --- Importaciones de tus modelos, repo, Result, ViewData, ViewModel, BottomBar ---
// --- Constantes para valores por defecto ---
private const val DEFAULT_STRING_VALUE = "No disponible"
private const val DEFAULT_REGION_NAME = "Región no especificada"
private const val DEFAULT_COMUNA_NAME = "Comuna no especificada"
private const val DEFAULT_SUCURSAL_NAME = "Sucursal no especificada"


@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class) // Añadir ExperimentalAnimationApi
@Composable
fun SucursalesScreen(
    navController: NavController,
    viewModel: SucursalesViewModel = hiltViewModel()
) {
    val uiState by viewModel.groupedSucursalesState.collectAsStateWithLifecycle()

    // --- Estado para gestionar qué elementos están expandidos ---
    // Guardamos los nombres de las regiones expandidas
    var expandedRegions by remember { mutableStateOf(setOf<String>()) }
    // Guardamos pares (RegionName, ComunaName) para comunas expandidas
    var expandedComunas by remember { mutableStateOf(setOf<Pair<String, String>>()) }


    // Carga inicial
    LaunchedEffect(Unit) {
        if (viewModel.groupedSucursalesState.value is Result.Loading) {
            viewModel.loadAndGroupSucursales()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sucursales") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás", tint = AppTheme.colors.amarillo)
                    }
                }
            )
        },
        bottomBar = {
            // Assuming you have a BottomBar composable, consistent with the image.
            Box(modifier = Modifier.padding(bottom = 16.dp))
            {
                BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            /*****############PARA ELIMINAR###############*********************
            // Botón de Refrescar
            Button(
                onClick = {
                    // Al refrescar, podrías querer contraer todo o mantener el estado
                    // Opción 1: Contraer todo al refrescar
                    // expandedRegions = setOf()
                    // expandedComunas = setOf()
                    // Opción 2: Mantener estado (acción por defecto aquí)
                    viewModel.loadAndGroupSucursales()
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                enabled = uiState !is Result.Loading
            ) {
                Text("Refrescar Datos")
            }##############################################################*/

            Spacer(modifier = Modifier.height(10.dp))

            // Contenido principal
            when (val state = uiState) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Result.Success -> {
                    val regions = state.data
                    if (regions.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontraron sucursales activas.")
                        }
                    } else {
                        // --- Lista Desplegable ---
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(regions, key = { it.nombreRegion ?: DEFAULT_REGION_NAME }) { region ->
                                val isRegionExpanded = expandedRegions.contains(region.nombreRegion)
                                ExpandableRegionItem(
                                    region = region,
                                    isExpanded = isRegionExpanded,
                                    onToggleRegion = { regionName ->
                                        expandedRegions = if (isRegionExpanded) {
                                            expandedRegions - regionName
                                        } else {
                                            expandedRegions + regionName
                                        }
                                        // Opcional: Contraer comunas si la región se contrae
                                        // if (!isRegionExpanded) {
                                        //    expandedComunas = expandedComunas.filterNot { it.first == regionName }.toSet()
                                        // }
                                    },
                                    // Pasamos el estado y el callback para las comunas anidadas
                                    expandedComunas = expandedComunas,
                                    onToggleComuna = { comunaKey -> // comunaKey es Pair<String, String>
                                        expandedComunas = if (expandedComunas.contains(comunaKey)) {
                                            expandedComunas - comunaKey
                                        } else {
                                            expandedComunas + comunaKey
                                        }
                                    }
                                )
                                Divider() // Separador entre regiones
                            }
                        }
                    }
                }
                is Result.Error -> {
                    // (Mismo manejo de error que antes)
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Error al cargar las sucursales", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = state.exception.localizedMessage ?: "Ocurrió un error desconocido", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}


// --- Composables para la lista desplegable ---

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableRegionItem(
    region: RegionViewData,
    isExpanded: Boolean,
    onToggleRegion: (String) -> Unit,
    expandedComunas: Set<Pair<String, String>>, // Recibe estado de comunas
    onToggleComuna: (Pair<String, String>) -> Unit // Recibe callback para comunas
) {
    val regionName = region.nombreRegion ?: DEFAULT_REGION_NAME
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        // --- Cabecera de Región (Clickable) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleRegion(regionName) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Empuja el icono al final
        ) {
            Text(
                text = regionName,
                style = MaterialTheme.typography.titleLarge, // Un poco más grande para la región
                fontWeight = FontWeight.Bold
            )
            ExpandCollapseIcon(isExpanded = isExpanded)
        }

        // --- Contenido Desplegable de la Región (Comunas) ---
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(start = 16.dp)) { // Indentación para comunas
                if (region.comunas.isEmpty()) {
                    Text("No hay comunas disponibles", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    region.comunas.forEach { comuna ->
                        val comunaName = comuna.nombreComuna ?: DEFAULT_COMUNA_NAME
                        // Clave única para el estado de expansión de esta comuna
                        val comunaKey = Pair(regionName, comunaName)
                        val isComunaExpanded = expandedComunas.contains(comunaKey)

                        ExpandableComunaItem(
                            comuna = comuna,
                            isExpanded = isComunaExpanded,
                            onToggle = { onToggleComuna(comunaKey) } // Llama al callback correcto
                        )
                        Divider(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) // Separador ligero entre comunas
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableComunaItem(
    comuna: ComunaViewData,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val comunaName = comuna.nombreComuna ?: DEFAULT_COMUNA_NAME
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        // --- Cabecera de Comuna (Clickable) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = comunaName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium // O SemiBold
            )
            ExpandCollapseIcon(isExpanded = isExpanded)
        }

        // --- Contenido Desplegable de la Comuna (Sucursales) ---
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) { // Mayor indentación para sucursales
                if (comuna.sucursales.isEmpty()) {
                    Text("No hay sucursales disponibles", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                } else {
                    comuna.sucursales.forEach { sucursal ->
                        // Usamos la Card de antes para mostrar detalles de la sucursal
                        SucursalItemCard(sucursal = sucursal)
                    }
                }
            }
        }
    }
}

// Icono simple que rota para indicar expansión/contracción
@Composable
fun ExpandCollapseIcon(isExpanded: Boolean) {
    Icon(
        imageVector = Icons.Filled.KeyboardArrowDown, // Siempre la flecha hacia abajo
        contentDescription = if (isExpanded) "Contraer" else "Expandir",
        modifier = Modifier.rotate(animateFloatAsState(targetValue = if (isExpanded) 180f else 0f).value) // Rota 180 grados si está expandido
    )
    // Alternativa: Cambiar icono
    // Icon(
    //    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
    //    contentDescription = if (isExpanded) "Contraer" else "Expandir"
    // )
}


// Renombrado para claridad, es la Card que muestra detalles de sucursal
@Composable
fun SucursalItemCard(sucursal: SucursalViewData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(text = sucursal.nombre ?: DEFAULT_SUCURSAL_NAME, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Dirección: ${sucursal.direccion ?: DEFAULT_STRING_VALUE}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = "Horario: ${sucursal.horario ?: DEFAULT_STRING_VALUE}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = "Teléfono: ${sucursal.telefono ?: "N/A"}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = "Correo: ${sucursal.correo ?: "N/A"}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}