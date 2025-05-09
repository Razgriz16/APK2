package com.example.oriencoop_score.view.mis_productos.cuenta_csocial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.oriencoop_score.model.MovimientosCsocial
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow
import com.example.oriencoop_score.view_model.MovimientosCsocialViewModel

@Composable
fun MovimientosCsocialScreen(
    numeroCuenta: Long,
    viewModel: MovimientosCsocialViewModel
) {
    val movimientosData by viewModel.movimientosData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
            error != null -> {
                Text(
                    text = error ?: "Error desconocido",
                    color = AppTheme.colors.rojo,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
            movimientosData != null -> {
                MovimientosCsocialList(movimientos = movimientosData?.data ?: emptyList())
            }
            else -> {
                Text(
                    text = "No hay datos disponibles",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MovimientosCsocialList(movimientos: List<MovimientosCsocial>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(movimientos) { movimiento ->
            MovimientoCsocialItem(movimiento = movimiento)
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
fun MovimientoCsocialItem(movimiento: MovimientosCsocial) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogMovimientosCsocial(movimiento = movimiento) {
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
                    text = movimiento.nombreabreviado,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = movimiento.fechapago,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (movimiento.escargo == "N") "+$${movimiento.monto}" else "-$${movimiento.monto}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (movimiento.escargo == "N") AppTheme.colors.verde else AppTheme.colors.rojo,
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
fun DialogMovimientosCsocial(
    movimiento: MovimientosCsocial,
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
                DetailRow(label = "Transacción:", value = movimiento.nombre)
                DetailRow(label = "Fecha:", value = movimiento.fechapago)
                DetailRow(label = "Sucursal:", value = movimiento.sucursal)
                DetailRow(
                    label = "Monto:",
                    value = if (movimiento.escargo == "N") "+$${movimiento.monto}" else "-$${movimiento.monto}",
                    valueColor = if (movimiento.escargo == "N") AppTheme.colors.verde else AppTheme.colors.rojo
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cerrar")
            }
        }
    )
}