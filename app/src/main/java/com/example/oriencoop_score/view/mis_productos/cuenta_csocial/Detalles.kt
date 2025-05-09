package com.example.oriencoop_score.view.mis_productos.cuenta_csocial


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detalles(
    accountNumber: String,
    balance: String,
    openingDate: String,
    accountType: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Número de cuenta en rectángulo ovalado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = AppTheme.colors.azul,
                    shape = RoundedCornerShape(8.dp) // Forma ovalada
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center // Texto centrado
        ) {
            Text(
                text = accountNumber,
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
                verticalArrangement = Arrangement.spacedBy(16.dp) // Consistent spacing
            ) {
                DetailRow("Saldo Contable", balance)
                HorizontalDivider()
                DetailRow("Fecha Apertura", openingDate)
                HorizontalDivider()
                DetailRow("Tipo", accountType)
            }
        }
    }
}

