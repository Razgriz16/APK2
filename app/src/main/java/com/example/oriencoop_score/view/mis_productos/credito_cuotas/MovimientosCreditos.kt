package com.example.oriencoop_score.view.mis_productos.credito_cuotas

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow
import com.example.oriencoop_score.view_model.MovimientosCreditosViewModel

@Composable
fun MovimientosCreditosScreen(
    movimientosCreditosViewModel: MovimientosCreditosViewModel,
) {
    val movimientosData by movimientosCreditosViewModel.movimientosData.collectAsState()
    val isLoading by movimientosCreditosViewModel.isLoading.collectAsState()
    val error by movimientosCreditosViewModel.error.collectAsState()
    Log.d("MovimientosCreditosScreen", "movimientosData: $movimientosData")

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
                Log.d("MovimientosCreditosScreen", "Movimientos: $movimientos")
                MovimientosCreditosList(movimientos)
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
fun MovimientosCreditosList(movimientos: List<MovimientosCreditos>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(movimientos) { movimiento ->
            MovimientosCreditosItem(movimiento = movimiento)
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        }
    }
}

@Composable
fun MovimientosCreditosItem(movimiento: MovimientosCreditos) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogMovimientosCreditos(movimiento = movimiento) {
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
                    text = movimiento.tipo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = movimiento.fecha,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$${movimiento.montoMovimiento}",
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
fun DialogMovimientosCreditos(
    movimiento: MovimientosCreditos,
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
                DetailRow(label = "Tipo:", value = movimiento.tipo)
                DetailRow(label = "Fecha:", value = movimiento.fecha)
                DetailRow(label = "Sucursal:", value = movimiento.sucursal)
                DetailRow(label = "Monto:", value = "$${movimiento.montoMovimiento}")
                DetailRow(label = "Usuario:", value = movimiento.usuario)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cerrar")
            }
        }
    )
}