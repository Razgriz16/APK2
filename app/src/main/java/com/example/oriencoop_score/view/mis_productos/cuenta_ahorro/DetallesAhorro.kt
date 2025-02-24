package com.example.oriencoop_score.view.mis_productos.cuenta_ahorro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


@Composable
fun CuentaAhorroItem(cuenta: CuentaAhorro, isSelected: Boolean, onClick: () -> Unit) {
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
                text = cuenta.NROCUENTA.toString(),
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

@Composable
fun DetallesAhorroScreen(cuenta: CuentaAhorro) {
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
            DetailRow(label = "Tipo:", value = cuenta.TIPOCUENTA)
            HorizontalDivider()
            DetailRow(label = "Saldo Disponible:", value = cuenta.SALDODISPONIBLE)
            HorizontalDivider()
            DetailRow(label = "Saldo Contable:", value = cuenta.SALDOCONTABLE)
            HorizontalDivider()
            DetailRow(label = "Fecha Apertura:", value = cuenta.FECHAAPERTURA)
            HorizontalDivider()
            DetailRow(label = "Sucursal:", value = cuenta.SUCURSAL)
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