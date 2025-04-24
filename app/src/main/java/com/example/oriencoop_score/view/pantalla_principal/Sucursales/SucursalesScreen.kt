package com.example.oriencoop_score.view.pantalla_principal.Sucursales

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.oriencoop_score.ui.theme.AppTheme // Assuming AppTheme is needed here for icon color
import com.example.oriencoop_score.view_model.SucursalesViewModel
import com.example.oriencoop_score.utility.Result // Assuming Result is needed here
import com.example.oriencoop_score.view.pantalla_principal.BottomBar

// Import the composables and constants from the new file
// If in the same package, direct use is possible, but explicit import is clearer sometimes
// If you moved them to a different package, you'd need explicit imports here.
// Example if in `com.example.oriencoop_score.ui.sucursal_list`:
// import com.example.oriencoop_score.ui.sucursal_list.ExpandableRegionItem
// import com.example.oriencoop_score.ui.sucursal_list.DEFAULT_REGION_NAME // etc.


@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class) // ExperimentalAnimationApi is used in the item composables, not strictly here
@Composable
fun SucursalesScreen(
    navController: NavController,
    viewModel: SucursalesViewModel = hiltViewModel()
) {
    val uiState by viewModel.groupedSucursalesState.collectAsStateWithLifecycle()

    // --- State for managing which elements are expanded ---
    // Guardamos los nombres de las regiones expandidas
    var expandedRegions by remember { mutableStateOf(setOf<String>()) }
    // Guardamos pares (RegionName, ComunaName) para comunas expandidas
    var expandedComunas by remember { mutableStateOf(setOf<Pair<String, String>>()) }

    // Carga inicial
    LaunchedEffect(Unit) {
        // Only load if not already loading or successful (prevents double load on config change)
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás", tint = AppTheme.colors.amarillo) // Assuming AppTheme is accessible/needed here
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = navController.currentDestination?.route ?: ""
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp) // Apply horizontal padding to the content area
        ) {
            // Removed the Refresh button as per comment - keep if needed

            Spacer(modifier = Modifier.height(10.dp)) // Space below AppBar/Button

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
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp) // Add space between region items
                        ) {
                            items(regions, key = { it.nombreRegion }) { region -> // Use const from new file
                                val isRegionExpanded = expandedRegions.contains(region.nombreRegion)
                                ExpandableRegionItem( // Call the composable from the other file
                                    region = region,
                                    isExpanded = isRegionExpanded,
                                    onToggleRegion = { regionName ->
                                        expandedRegions = if (isRegionExpanded) {
                                            expandedRegions - regionName
                                        } else {
                                            expandedRegions + regionName
                                        }
                                        // Optional: Contraer comunas si la región se contrae
                                        // if (!isRegionExpanded) {
                                        //    expandedComunas = expandedComunas.filterNot { it.first == regionName }.toSet()
                                        // }
                                    },
                                    // Pasamos el estado y el callback para las comunas anidadas
                                    expandedComunas = expandedComunas,
                                    onToggleComuna = { comunaKey -> // comunaKey is Pair<String, String>
                                        expandedComunas = if (expandedComunas.contains(comunaKey)) {
                                            expandedComunas - comunaKey
                                        } else {
                                            expandedComunas + comunaKey
                                        }
                                    }
                                )
                                // Divider() // Moved Divider inside ExpandableRegionItem if needed between communes
                            }
                        }
                    }
                }
                is Result.Error -> {
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

