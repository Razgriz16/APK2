package com.example.oriencoop_score.view.mis_productos.cuenta_ahorro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import com.example.oriencoop_score.model.MovimientosAhorro
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view_model.MovimientosAhorroViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
/*
@Composable
fun MovimientosAhorroScreen(
    movimientosAhorroViewModel: MovimientosAhorroViewModel,
    selectedAccount: Long
) {
    // Observamos los estados del ViewModel
    val movimientos by movimientosAhorroViewModel.movimientos.collectAsState()
    val isLoading by movimientosAhorroViewModel.isLoading.collectAsState()
    val error by movimientosAhorroViewModel.error.collectAsState()

    // Filtramos los movimientos según la cuenta seleccionada.
    // Se asume que cada MovimientosAhorro tiene la propiedad NROCUENTA: Long.
    val filteredMovimientos = movimientos.filter { it.NROCUENTA == selectedAccount }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = error ?: "Error desconocido", color = MaterialTheme.colorScheme.error)
            }
        }

        else -> {
            MovimientosList(filteredMovimientos)
        }
    }
}

@Composable
fun MovimientosList(movimientos: List<MovimientosAhorro>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp), // Reduce horizontal padding
        verticalArrangement = Arrangement.spacedBy(0.dp) // No spacing between items
    ) {
        items(movimientos) { movimiento ->
            MovimientoItem(movimiento = movimiento)
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray) // Add divider
        }
    }
}

@Composable
fun MovimientoItem(movimiento: MovimientosAhorro) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogMovimientosAhorro(movimiento = movimiento) {
            showDialog = false
        }
    }

    Card(  // Use Card without elevation for a flatter look.
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),  // Remove elevation
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Use surface color

    ) {
        Row( // Use Row for horizontal layout
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp), // Adjust padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Space between elements
        ) {
            Column(
                modifier = Modifier.weight(1f)  // Column takes available space
            ) {
                Text(
                    text = movimiento.NOMBRETRANSACABRV,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp)) // Reduce spacer
                Text(
                    text = movimiento.FECHAMOV, // Removed "Fecha: "
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(  // amount and plus icon
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                Text(
                    text = if (movimiento.CARGOABONO == "A") "+$${movimiento.MONTO}" else "-$${movimiento.MONTO}",
                    style = MaterialTheme.typography.bodyLarge,  // or titleMedium?
                    color = if (movimiento.CARGOABONO == "A") AppTheme.colors.verde else AppTheme.colors.rojo,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier

                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ver más",
                    tint = if (showDialog) AppTheme.colors.amarillo else AppTheme.colors.azul,
                    modifier = Modifier.size(20.dp)
                )

            }
        }
    }
}

@Composable
fun DialogMovimientosAhorro(
    movimiento: MovimientosAhorro,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Detalle del Movimiento",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            // Mostramos la información en filas, usando DetailRow para alinear etiqueta y valor
            Column {
                DetailRow(label = "Transacción:", value = movimiento.NOMBRETRANSAC)
                DetailRow(label = "Fecha:", value = movimiento.FECHAMOV)
                DetailRow(label = "Sucursal:", value = movimiento.NOMBRESUCURSAL)
                DetailRow(
                    label = "Monto:",
                    value = if (movimiento.CARGOABONO == "A")
                        "+$${movimiento.MONTO}"
                    else
                        "-$${movimiento.MONTO}",
                    valueColor = if (movimiento.CARGOABONO == "A") AppTheme.colors.verde else AppTheme.colors.rojo
                )
                // Puedes agregar más detalles si tu modelo tiene más propiedades
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cerrar")
            }
        }
    )
}
*/