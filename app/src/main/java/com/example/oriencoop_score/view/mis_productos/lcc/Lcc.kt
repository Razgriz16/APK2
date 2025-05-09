package com.example.oriencoop_score.view.mis_productos.lcc

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.model.MovimientosLcc
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.FacturasLccViewModel
import com.example.oriencoop_score.view_model.LccViewModel
import com.example.oriencoop_score.view_model.MovimientosLccViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lcc(
    navController: NavController
) {
    val lccViewModel: LccViewModel = hiltViewModel()
    val lccData by lccViewModel.lccData.collectAsState()
    val isLoading by lccViewModel.isLoading.collectAsState()
    val error by lccViewModel.error.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Línea de Crédito a Cuotas",
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        fontSize = AppTheme.typography.normal.fontSize
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.colors.amarillo
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = error ?: "Error desconocido",
                                color = Color.Red
                            )
                        }
                    }
                    else -> {
                        lccData?.data?.forEach { item ->
                            DetallesLcc(
                                accountNumber = item.codigo,
                                cupoUtilizado = "$ ${item.cupoutilizado}",
                                cupoDisponible = "$ ${item.cupodisponible}",
                                diasMora = item.diasmora,
                                ultimoPago = item.ultimo_pago
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Commented out facturas section
            /*
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showFacturasDialog = true },
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Facturas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ver todas las facturas",
                            tint = AppTheme.colors.azul
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            */

            // Commented out movimientos section
            /*
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Movimientos",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = { showAllMovimientosDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ver todos los movimientos",
                            tint = AppTheme.colors.azul
                        )
                    }
                }
            }
            item {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text(text = error ?: "Error desconocido", color = Color.Red)
                    }
                } else {
                    Box(modifier = Modifier.height(200.dp)) {
                        MovimientosListLcc(movimientos = movimientos)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            */
        }

        // Commented out facturas dialog
        /*
        if (showFacturasDialog) {
            FacturasLccDialog(
                facturas = facturas,
                isLoading = isLoading,
                error = error,
                onDismiss = { showFacturasDialog = false }
            )
        }
        */

        // Commented out movimientos dialog
        /*
        if (showAllMovimientosDialog) {
            AllMovimientosDialogLcc(
                movimientos = movimientos,
                isLoading = isLoading,
                error = error,
                onDismiss = { showAllMovimientosDialog = false }
            )
        }
        */
    }
}