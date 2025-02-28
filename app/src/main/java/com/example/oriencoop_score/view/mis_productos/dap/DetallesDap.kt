package com.example.oriencoop_score.view.mis_productos.dap

import DapResponse
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oriencoop_score.R
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.utility.formatNumberWithDots2


@Composable
fun DapItem(cuenta: DapResponse, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AppTheme.colors.azul else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cuenta.numeroDeposito,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (isSelected) Color.White else Color.Black
            )
            val dropdownIcon: Painter = if (isSelected) {
                painterResource(id = R.drawable.chevronup) // Your "up" image
            } else {
                painterResource(id = R.drawable.chevrondown) // Your "down" image
            }
            Image(
                painter = dropdownIcon,
                contentDescription = if (isSelected) "Collapse" else "Expand",
                modifier = Modifier.size(24.dp), // Adjust size as needed
                contentScale = ContentScale.Fit // or ContentScale.Crop, etc.
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DetallesDap(cuenta: DapResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp), // padding horizontal para alinear con el item de arriba
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailRow(label = "Tipo:", value = cuenta.nombreProducto)
            HorizontalDivider()
            cuenta.fechaActivacion?.let { DetailRow(label = "Fecha Activación:", value = it) }
            HorizontalDivider()
            cuenta.fechaVencimiento?.let { DetailRow(label = "Fecha Vencimietno:", value = it) }
            HorizontalDivider()
            DetailRow(label = "Plazo pactado? (Días):", value = cuenta.plazoPactado.toString())
            HorizontalDivider()
            DetailRow(label = "Estado:", value = cuenta.nombreEstado)
            HorizontalDivider()
            DetailRow(label = "Sucursal:", value = cuenta.sucursalOrigen.toString())
            HorizontalDivider()
            val montoInteresInversion = formatNumberWithDots2(cuenta.montoInversion)
            DetailRow(label = "Monto Inversión:", value = montoInteresInversion)
            HorizontalDivider()
            val montoInteresInteres = formatNumberWithDots2(cuenta.montoInteres)
            DetailRow(label = "Monto Interés:", value = montoInteresInteres)
            HorizontalDivider()
            val montoRescate = formatNumberWithDots2(cuenta.montoInversion + cuenta.montoInteres)
            DetailRow(label = "Monto Rescate:", value = montoRescate, valueColor = AppTheme.colors.azul)

        }
    }
}

// mueve los nombres y los labes a los etremos
@Composable
fun DetailRow(
    label: String,
    value: String,
    labelColor: Color = Color.Black, // Color por defecto negro
    valueColor: Color = Color.Black  // Color por defecto negro
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            color = labelColor // Aplica el color al texto de la etiqueta
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = valueColor // Aplica el color al texto del valor
        )
    }
}