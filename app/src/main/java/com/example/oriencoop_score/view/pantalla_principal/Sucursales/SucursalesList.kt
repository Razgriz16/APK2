package com.example.oriencoop_score.view.pantalla_principal.Sucursales
/*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oriencoop_score.model.BranchDetail
import com.example.oriencoop_score.model.RegionViewData

@OptIn(ExperimentalFoundationApi::class) // Para stickyHeader
@Composable
fun BranchList(
    regions: List<RegionViewData>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp) // Espacio arriba y abajo de la lista
    ) {
        regions.forEach { region ->
            // --- Cabecera de Región (Fija) ---
            stickyHeader {
                RegionHeader(name = region.name)
            }

            // --- Comunas y Sucursales dentro de la Región ---
            region.communes.forEach { commune ->
                // Espaciador opcional antes de la comuna para separarla de la cabecera de región
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // --- Cabecera de Comuna (No fija) ---
                item {
                    CommuneHeader(name = commune.name)
                }

                // --- Lista de Sucursales para esta Comuna ---
                items(
                    items = commune.branches,
                    key = { branch -> branch.id } // Clave única para cada sucursal
                ) { branch ->
                    BranchItem(branch = branch, modifier = Modifier.padding(bottom = 8.dp))
                }

                // Espaciador opcional después de la última sucursal de una comuna
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
fun RegionHeader(name: String, modifier: Modifier = Modifier) {
    Surface( // Surface para poder poner sombra o color de fondo distinto
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant, // Un color distinto para la cabecera
        shadowElevation = 2.dp // Sombra ligera
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium, // Estilo más prominente
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun CommuneHeader(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleSmall, // Ligeramente menos prominente que la región
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 16.dp, top = 8.dp, bottom = 4.dp) // Indentación
    )
}

@Composable
fun BranchItem(branch: BranchDetail, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Padding horizontal para la tarjeta
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding interno de la tarjeta
                .fillMaxWidth()
        ) {
            Text(
                text = branch.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            InfoRow(label = "Dirección:", value = branch.address)
            branch.phone?.let { phone ->
                Spacer(modifier = Modifier.height(4.dp))
                InfoRow(label = "Teléfono:", value = phone)
            }
            branch.agent?.let { agent ->
                Spacer(modifier = Modifier.height(4.dp))
                InfoRow(label = "Agente:", value = agent)
            }
            branch.schedule?.let { schedule ->
                Spacer(modifier = Modifier.height(4.dp))
                // TODO: Parsear el horario si contiene <br> u otro formato
                // Por ahora, lo mostramos tal cual. Podrías reemplazar "<br>" por "\n"
                val formattedSchedule = schedule.replace("<br>", "\n", ignoreCase = true)
                InfoRow(label = "Horarios:", value = formattedSchedule)
            }

            // Opcional: Botón para ver mapa (requiere lógica adicional)
            // Spacer(modifier = Modifier.height(8.dp))
            // TextButton(onClick = { /* TODO: Implementar acción de mapa */ }) {
            //     Text("Ver en mapa")
            // }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row {
        Text(
            text = "$label ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant // Color ligeramente diferente para la etiqueta
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}*/