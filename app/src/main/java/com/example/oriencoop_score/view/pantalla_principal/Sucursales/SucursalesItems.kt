package com.example.oriencoop_score.view.pantalla_principal.Sucursales

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oriencoop_score.model.ComunaViewData
import com.example.oriencoop_score.model.RegionViewData
import com.example.oriencoop_score.model.SucursalViewData

// --- Constants for default values ---
internal const val DEFAULT_STRING_VALUE_CONST = "No disponible"
internal const val DEFAULT_REGION_NAME_CONST = "Región no especificada"
internal const val DEFAULT_COMUNA_NAME_CONST = "Comuna no especificada"
internal const val DEFAULT_SUCURSAL_NAME_CONST = "Sucursal no especificada"


@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun ExpandableRegionItem( // Use internal to indicate scope within the module/package
    region: RegionViewData,
    isExpanded: Boolean,
    onToggleRegion: (String) -> Unit,
    expandedComunas: Set<Pair<String, String>>, // Recibe estado de comunas
    onToggleComuna: (Pair<String, String>) -> Unit // Recibe callback para comunas
) {
    val regionName = region.nombreRegion ?: DEFAULT_REGION_NAME_CONST
    Column(modifier = Modifier
        .fillMaxWidth() // Ensure it fills width to make clickable area large
        .padding(vertical = 4.dp)) {
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
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top), // Start expansion from the top
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top) // Shrink towards the top
        ) {
            Column(modifier = Modifier
                .fillMaxWidth() // Ensure inner column also fills width
                .padding(start = 16.dp)) { // Indentación para comunas
                if (region.comunas.isEmpty()) {
                    Text("No hay comunas disponibles", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    region.comunas.forEach { comuna ->
                        val comunaName = comuna.nombreComuna ?: DEFAULT_COMUNA_NAME_CONST
                        // Clave única para el estado de expansión de esta comuna
                        val comunaKey = Pair(regionName, comunaName)
                        val isComunaExpanded = expandedComunas.contains(comunaKey)

                        ExpandableComunaItem(
                            comuna = comuna,
                            isExpanded = isComunaExpanded,
                            onToggle = { onToggleComuna(comunaKey) } // Llama al callback correcto
                        )
                        // Add a subtle separator *after* each comuna item, except perhaps the last one
                        // Or better, let the parent LazyColumn handle spacing if preferred
                        // Divider(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) // Separator lighter between comunas
                    }
                }
            }
        }
    }
    Divider() // Separator between regions
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun ExpandableComunaItem( // Use internal
    comuna: ComunaViewData,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val comunaName = comuna.nombreComuna ?: DEFAULT_COMUNA_NAME_CONST
    Column(modifier = Modifier
        .fillMaxWidth() // Ensure it fills width
        .padding(vertical = 4.dp)) {
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
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top), // Start expansion from the top
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top) // Shrink towards the top
        ) {
            Column(modifier = Modifier
                .fillMaxWidth() // Ensure inner column fills width
                .padding(start = 16.dp, top = 4.dp)) { // Mayor indentación para sucursales
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
internal fun ExpandCollapseIcon(isExpanded: Boolean) { // Use internal
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "expansionIconRotation")
    Icon(
        imageVector = Icons.Filled.KeyboardArrowDown, // Siempre la flecha hacia abajo
        contentDescription = if (isExpanded) "Contraer" else "Expandir",
        modifier = Modifier.rotate(rotationAngle) // Rota 180 degrees if expanded
    )
}


// Renombrado para claridad, es la Card que muestra detalles de sucursal
@Composable
internal fun SucursalItemCard(sucursal: SucursalViewData) { // Use internal
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(text = sucursal.nombre ?: DEFAULT_SUCURSAL_NAME_CONST, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Dirección: ${sucursal.direccion ?: DEFAULT_STRING_VALUE_CONST}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = "Horario: ${sucursal.horario ?: DEFAULT_STRING_VALUE_CONST}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = "Teléfono: ${sucursal.telefono ?: DEFAULT_STRING_VALUE_CONST}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray) // Use default value for phone/email too
            Text(text = "Correo: ${sucursal.correo ?: DEFAULT_STRING_VALUE_CONST}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}