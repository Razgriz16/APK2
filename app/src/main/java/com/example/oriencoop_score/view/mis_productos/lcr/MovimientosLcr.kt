package com.example.oriencoop_score.view.mis_productos.lcr

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oriencoop_score.model.MovimientosLcr
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow


@Composable
fun MovimientosListLcr(movimientos: List<MovimientosLcr>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),  // Keep fillMaxSize
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp), // Adjust padding
        verticalArrangement = Arrangement.spacedBy(0.dp) // No spacing, use dividers
    ) {
        items(movimientos) { movimiento ->
            MovimientoItemLcr(movimiento = movimiento)
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp) // Add divider
        }
    }
}

@Composable
fun MovimientoItemLcr(movimiento: MovimientosLcr) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogMovimientosLcr(movimiento = movimiento) {
            showDialog = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Flat card
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Use surface color

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp), // Adjust padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Key for alignment
        ) {
            Column(
                modifier = Modifier.weight(1f) // Column takes up available space
            ) {
                Text(
                    text = movimiento.NOMBREMOVIMIENTO,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp)) // Reduced spacer
                Text(
                    text = movimiento.FECHAVENCIMIENTO, // Removed "Fecha: "
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                Text(
                    text = movimiento.MONTO,
                    style = MaterialTheme.typography.bodyLarge,  // Or titleMedium, consistent style
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ver más",
                    tint = if (showDialog) AppTheme.colors.amarillo else AppTheme.colors.azul, //consistent
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun DialogMovimientosLcr(
    movimiento: MovimientosLcr,
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
                DetailRow(label = "Transacción:", value = movimiento.DESCRIPCION)
                DetailRow(label = "Fecha contable:", value = movimiento.FECHACONTABLE)
                DetailRow(label = "Fecha vencimiento:", value = movimiento.FECHAVENCIMIENTO)
                //(label = "Sucursal:", value = movimiento.SUCURSAL)
                DetailRow(label = "Monto:", value = movimiento.MONTO)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cerrar")
            }
        }
    )
}

