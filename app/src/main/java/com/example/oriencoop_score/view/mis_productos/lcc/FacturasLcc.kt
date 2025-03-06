package com.example.oriencoop_score.view.mis_productos.lcc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.oriencoop_score.R
import com.example.oriencoop_score.model.FacturasLcc
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.ui.theme.amarillo
import com.example.oriencoop_score.ui.theme.rojo
import com.example.oriencoop_score.ui.theme.verde
import com.example.oriencoop_score.utility.formatNumberWithDots2
import com.example.oriencoop_score.view.mis_productos.cuenta_ahorro.DetailRow
@Composable
fun FacturasLccDialog(
    facturas: List<FacturasLcc>,
    isLoading: Boolean,
    error: String?,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = amarillo
                        )
                    }
                    Text(
                        text = "Facturas",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp)) // Keep the spacer for consistent layout
                }

                HorizontalDivider()

                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = error ?: "Error desconocido", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    facturas.isEmpty() -> {  // Check for empty list *here*
                        EmptyFacturasView() // Use a dedicated Composable
                    }
                    else -> {
                        FacturasList(facturas = facturas)
                    }
                }
            }
        }
    }
}


@Composable
fun FacturasList(facturas: List<FacturasLcc>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // Add padding around the list
        verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
    ) {
        items(facturas) { factura ->
            FacturaItem(factura = factura)
        }
    }
}

@Composable
fun FacturaItem(factura: FacturasLcc) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)), // Rounded corners for the card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) , // Add a subtle shadow

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp) // Add padding inside the card
        ) {

            // Title (Estado) -  Make it stand out
            Text(
                text = factura.ESTADO,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, // Make the title bold
                color = when (factura.ESTADO.uppercase()) { // Conditional coloring based on status
                    "VIGENTE" -> verde
                    "MOROSA" -> Color(0xFFFFA500) // Orange
                    "VENCIDA" -> rojo
                    else -> MaterialTheme.colorScheme.onSurface // Default color
                }
            )
            Spacer(modifier = Modifier.height(8.dp)) // Add spacing between title and details

            DetailRow(label = "Fecha Vencimiento:", value = factura.FECHAVENCIMIENTO)
            DetailRow(label = "Monto Facturado:", value = "$${factura.MONTOFACTURADO}")
            DetailRow(label = "Días Mora:", value = factura.DIASMORA)

        }
    }
}


@Composable
fun EmptyFacturasView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tick), // Replace with your image resource
            contentDescription = "No invoices",
            modifier = Modifier.size(120.dp) // Adjust size as needed
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No hay facturas",
            style = MaterialTheme.typography.titleMedium, // Or any style you prefer
            textAlign = TextAlign.Center
        )
    }
}

// Preview with data
@Preview(showBackground = true)
@Composable
fun FacturasLccDialogPreviewWithData() {
    val sampleFacturas = listOf(
        FacturasLcc("2024-03-15", "Pagada", "$100.00", "0"),
        FacturasLcc("2024-02-28", "Pendiente", "$50.00", "15"),
        FacturasLcc("2024-01-10", "Vencida", "$75.00", "60")
    )
    MaterialTheme { // Wrap with MaterialTheme for correct styling
        FacturasLccDialog(facturas = sampleFacturas, isLoading = false, error = null) {}
    }
}

// Preview without data (empty list, showing EmptyFacturasView)
@Preview(showBackground = true)
@Composable
fun FacturasLccDialogPreviewEmpty() {
    FacturasLccDialog(facturas = emptyList(), isLoading = false, error = null) {}

}

@Preview(showBackground = true)
@Composable
fun FacturasLccDialogPreviewLoading() {

    FacturasLccDialog(facturas = emptyList(), isLoading = true, error = null) {}

}
/*
@Composable
fun FacturasDesplegable(facturas: List<FacturasLcc>) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
            ,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Facturas",
                    style = AppTheme.typography.normal,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Contraer" else "Expandir",
                    tint = AppTheme.colors.azul, // Use your theme color
                    modifier = Modifier.size(24.dp)
                )
            }
            // Contenido expandible
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        // .background(Color.LightGray.copy(alpha = 0.2f)) // Optional light background
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    facturas.forEach { factura ->
                        FacturaItem(factura)
                        HorizontalDivider() // Add divider between items
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun FacturaItem(factura: FacturasLcc) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        DetailRow("Fecha Vencimiento", factura.FECHAVENCIMIENTO)
        DetailRow("Estado", factura.ESTADO)
        DetailRow("Días Mora", factura.DIASMORA)
        DetailRow("Monto Facturado", factura.MONTOFACTURADO)

    }
}*/