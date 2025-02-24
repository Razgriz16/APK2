package com.example.oriencoop_score.view.mis_productos.credito_cuotas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oriencoop_score.R
import com.example.oriencoop_score.model.CreditoCuota
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CreditoCuotaItem(cuenta: CreditoCuota, isSelected: Boolean, onClick: () -> Unit) {
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
fun DetallesCreditoCuotas(cuenta: CreditoCuota) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp) // More spacing
        ) {
            DetailRow(label = "Tipo", value = cuenta.TIPOCUENTA)
            HorizontalDivider()
            DetailRow(label = "Número de cuotas", value = "${cuenta.NUMEROCUOTAS}")
            HorizontalDivider()
            DetailRow(label = "Monto crédito", value = cuenta.MONTOCREDITO)
            HorizontalDivider()
            DetailRow(label = "Valor Cuota", value = cuenta.VALORCUOTA)
            HorizontalDivider()
            DetailRow(label = "Prox Vencimiento", value = cuenta.PROXVENCIMIENTO)
        }
    }
}
@Preview
@Composable
fun preview(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // More spacing
        ) {
            DetailRow(label = "Tipo", value = "premium")
            HorizontalDivider() // Add dividers
            DetailRow(label = "Número de cuotas", value = "30")
            HorizontalDivider()
            DetailRow(label = "Monto crédito", value = "100.000")
            HorizontalDivider()
            DetailRow(label = "Valor Cuota", value = "1000")
            HorizontalDivider()
            DetailRow(label = "Prox Vencimiento", value = "14/02/25")
        }
    }
}


/*
@Composable
fun DetallesCreditoCuotas(
    nroCuenta: String,
    tipoCuenta: String,
    nroCuotas: String,
    montoCredito: String,
    valorCuota: String,
    fechaVencimiento: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.azul)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Número de cuenta
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = nroCuenta,
                    style = AppTheme.typography.normal,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "Info",
                    tint = AppTheme.colors.amarillo,
                    modifier = Modifier
                        .clickable {}
                        .size(24.dp)
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            // Saldo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tipo Cuenta",
                    color = Color.White,
                    style = AppTheme.typography.normal,
                    modifier = Modifier.weight(1f),

                )
                Text(
                    text = tipoCuenta,
                    color = Color.White,
                    style = AppTheme.typography.normal,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Número de Cuotas",
                    color = Color.White,
                    style = AppTheme.typography.normal

                )
                Text(
                    text = nroCuotas,
                    color = Color.White,
                    style = AppTheme.typography.normal

                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Monto Crédito",
                    color = Color.White,
                    style = AppTheme.typography.normal
                )
                Text(
                    text = montoCredito,
                    color = Color.White,
                    style = AppTheme.typography.normal
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Valor Cuota",
                    color = Color.White,
                    style = AppTheme.typography.normal
                )
                Text(
                    text = valorCuota,
                    color = Color.White,
                    style = AppTheme.typography.normal
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Próximo Vencimiento",
                    color = Color.White,
                    style = AppTheme.typography.normal
                )
                Text(
                    text = fechaVencimiento,
                    color = Color.White,
                    style = AppTheme.typography.normal
                )
            }
        }
    }
}
*/