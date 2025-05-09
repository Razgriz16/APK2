package com.example.oriencoop_score.view.mis_productos.lcc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow


@Composable
fun DetallesLcc(
    accountNumber: Long,
    cupoUtilizado: String,
    cupoDisponible: String,
    diasMora: Int,
    ultimoPago: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Número de cuenta en rectángulo azul
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = AppTheme.colors.azul,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$accountNumber",
                style = AppTheme.typography.normal,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailRow("Cupo Utilizado", cupoUtilizado)
                HorizontalDivider()
                DetailRow("Cupo Disponible", cupoDisponible)
                HorizontalDivider()
                DetailRow("Días de Mora", diasMora.toString())
                HorizontalDivider()
                DetailRow("Último Pago", ultimoPago)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetallesLccPreview() {
    MaterialTheme {
        DetallesLcc(
            accountNumber = 1234567890L,
            cupoUtilizado = "$2,000",
            cupoDisponible = "$8,000",
            diasMora = 5,
            ultimoPago = "2025-04-15"
        )
    }
}