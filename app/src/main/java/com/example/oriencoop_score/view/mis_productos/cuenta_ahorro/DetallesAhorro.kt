package com.example.oriencoop_score.view.mis_productos.cuenta_ahorro

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.oriencoop_score.model.CuentaAhorro
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.R
import com.example.oriencoop_score.utility.formatCuenta

@Composable
fun CuentaAhorroItem(cuentaFormateada: String, isSelected: Boolean, onClick: () -> Unit) {
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
                text = formatCuenta(cuentaFormateada), // Changed from NROCUENTA
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (isSelected) Color.White else Color.Black
            )
            val dropdownIcon: Painter = if (isSelected) {
                painterResource(id = R.drawable.chevron_up_yellow)
            } else {
                painterResource(id = R.drawable.chevron_down_yellow)
            }
            Image(
                painter = dropdownIcon,
                contentDescription = if (isSelected) "Collapse" else "Expand",
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun DetallesAhorroScreen(cuenta: CuentaAhorro) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailRow(label = "Tipo:", value = cuenta.nombreProducto) // Changed from TIPOCUENTA
            HorizontalDivider()
            DetailRow(label = "Saldo Disponible:", value = cuenta.saldoDisponible.toString()) // Changed from SALDODISPONIBLE
            HorizontalDivider()
            DetailRow(label = "Saldo Contable:", value = cuenta.saldoContable.toString()) // Changed from SALDOCONTABLE
            HorizontalDivider()
            DetailRow(label = "Fecha Apertura:", value = cuenta.fechaApertura.toString()) // Changed from FECHAAPERTURA
            HorizontalDivider()
            DetailRow(label = "Sucursal:", value = cuenta.codigoSucursal.toString()) // Changed from SUCURSAL, assuming codigoSucursal
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    labelColor: Color = Color.Black,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            color = labelColor
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = valueColor
        )
    }
}