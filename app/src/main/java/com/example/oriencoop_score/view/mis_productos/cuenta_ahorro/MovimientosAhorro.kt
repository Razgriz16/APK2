package com.example.oriencoop_score.view.mis_productos.cuenta_ahorro

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import com.example.oriencoop_score.model.MovimientosAhorro
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view_model.MovimientosAhorroViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp

@Composable
fun MovimientosAhorroScreen(
    movimientosAhorroViewModel: MovimientosAhorroViewModel,
) {
    val movimientosData by movimientosAhorroViewModel.movimientosData.collectAsState()
    val isLoading by movimientosAhorroViewModel.isLoading.collectAsState()
    val error by movimientosAhorroViewModel.error.collectAsState()
    Log.d("MovimientosAhorroScreen", "movimientosData: $movimientosData")

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
            movimientosData?.data?.let { movimientos ->
                Log.d("MovimientosAhorroScreen", "Movimientos: $movimientos")
                MovimientosAhorroList(movimientos)
            } ?: Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No hay movimientos disponibles")
            }
        }
    }
}

@Composable
fun MovimientosAhorroList(movimientos: List<MovimientosAhorro>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(movimientos) { movimiento ->
            MovimientosAhorroItem(movimiento = movimiento)
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}

@Composable
fun MovimientosAhorroItem(movimiento: MovimientosAhorro) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogMovimientosAhorro(movimiento = movimiento) {
            showDialog = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movimiento.nombreTransaccion,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = movimiento.fechaEfectiva,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$${movimiento.monto}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTheme.colors.negro,
                    fontWeight = FontWeight.Bold
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
            Column {
                DetailRow(label = "Transacción:", value = movimiento.nombreTransaccion)
                DetailRow(label = "Fecha:", value = movimiento.fechaEfectiva)
                DetailRow(label = "Sucursal:", value = movimiento.sucursal)
                DetailRow(label = "Monto:", value = "$${movimiento.monto}")
                DetailRow(label = "Usuario:", value = movimiento.usuario)
                DetailRow(label = "Tipo:", value = movimiento.tipo)
                DetailRow(label = "Sucursal Origen:", value = movimiento.sucursalOrigen)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cerrar")
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}