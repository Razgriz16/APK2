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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.oriencoop_score.model.ComunaViewData
import com.example.oriencoop_score.model.RegionViewData
import com.example.oriencoop_score.model.SucursalViewData
import com.example.oriencoop_score.view_model.SucursalesViewModel
import com.example.oriencoop_score.utility.Result


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SucursalesScreen(navController: NavController,
    viewModel: SucursalesViewModel = hiltViewModel() // Inyecta el ViewModel
) {
    // Recoge el estado del StateFlow de forma segura respecto al ciclo de vida
    val uiState by viewModel.groupedSucursalesState.collectAsStateWithLifecycle()

    // Llama a la carga inicial la primera vez que el Composable entra en la composición
    // si el estado aún es Loading (o según tu lógica inicial)
    LaunchedEffect(Unit) {
        if (uiState is Result.Loading) { // Evita recargar si ya hay datos o error en recomposiciones
            viewModel.loadAndGroupSucursales()
        }
        // O si siempre quieres cargar al entrar:
        // viewModel.loadAndGroupSucursales()
    }

    Scaffold( // O usa un Column directamente si no necesitas AppBar/FAB etc.
        topBar = {
            TopAppBar(title = { Text("Sucursales Agrupadas") })
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues) // Aplica padding del Scaffold
                .fillMaxSize()
                .padding(horizontal = 16.dp) // Padding horizontal general
        ) {
            // Botón de Refrescar siempre visible
            Button(
                onClick = { viewModel.loadAndGroupSucursales() },
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp) // Espacio arriba y abajo del botón
            ) {
                Text("Refrescar Datos")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Muestra contenido basado en el estado (Loading, Success, Error)
            when (val state = uiState) {
                is Result.Loading -> {
                    // Muestra indicador de carga centrado
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando sucursales...")
                    }
                }
                is Result.Success -> {
                    val regions = state.data
                    if (regions.isEmpty()) {
                        // Mensaje si no hay datos activos para mostrar
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron sucursales activas.")
                        }
                    } else {
                        // Lista perezosa para mostrar los datos agrupados
                        GroupedSucursalesList(regions = regions)
                    }
                }
                is Result.Error -> {
                    // Muestra mensaje de error
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Error al cargar los datos",
                                color = MaterialTheme.colorScheme.error, // Usa color de error del tema
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.exception.localizedMessage ?: "Ocurrió un error desconocido",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupedSucursalesList(regions: List<RegionViewData>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre elementos de primer nivel (Regiones)
    ) {
        regions.forEach { region ->
            // --- Bloque para la Región ---
            item {
                RegionItem(region = region)
            }
        }
    }
}

@Composable
fun RegionItem(region: RegionViewData) {
    Column {
        // Nombre de la Región
        Text(
            text = region.nombreRegion,
            style = MaterialTheme.typography.headlineSmall, // Estilo más grande para la región
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Lista de Comunas dentro de la Región
        region.comunas.forEach { comuna ->
            ComunaItem(comuna = comuna)
            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre comunas
        }
    }
}

@Composable
fun ComunaItem(comuna: ComunaViewData) {
    Column(modifier = Modifier.padding(start = 16.dp)) { // Indentación para la comuna
        // Nombre de la Comuna
        Text(
            text = comuna.nombreComuna,
            style = MaterialTheme.typography.titleMedium, // Estilo intermedio para comuna
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Lista de Sucursales dentro de la Comuna
        comuna.sucursales.forEach { sucursal ->
            SucursalItem(sucursal = sucursal)
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre sucursales
        }
    }
}

@Composable
fun SucursalItem(sucursal: SucursalViewData) {
    Column(modifier = Modifier.padding(start = 16.dp)) { // Indentación adicional para sucursal
        // Nombre de la Sucursal
        Text(
            text = sucursal.nombre,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        // Detalles de la Sucursal
        Text(
            text = "Dirección: ${sucursal.direccion}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray // Color más suave para detalles
        )
        Text(
            text = "Horario: ${sucursal.horario}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        // Puedes añadir teléfono, correo, etc. si los incluiste en SucursalViewData
        // if (sucursal.telefono != null) {
        //     Text(text = "Tel: ${sucursal.telefono}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        // }
    }
}