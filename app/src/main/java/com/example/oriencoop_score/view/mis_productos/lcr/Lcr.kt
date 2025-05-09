package com.example.oriencoop_score.view.mis_productos.lcr

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.model.Lcr
import com.example.oriencoop_score.model.MovimientosLcr
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.LcrViewModel
import com.example.oriencoop_score.view_model.MovimientosLcrViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lcr(
    navController: NavController
) {
    val lcrViewModel: LcrViewModel = hiltViewModel()
    val movimientosLcrViewModel: MovimientosLcrViewModel = hiltViewModel()

    val lcrData by lcrViewModel.lcrData.collectAsState()
    val isLoading by lcrViewModel.isLoading.collectAsState()
    val error by lcrViewModel.error.collectAsState()

    // Parse numeroCuenta from lcrData
    val numeroCuenta = lcrData?.data?.firstOrNull()?.numerocuenta?.let { account ->
        parseNumeroCuenta(account)
    } ?: 0

    // Trigger data fetch if numeroCuenta is valid
    if (numeroCuenta > 0) {
        movimientosLcrViewModel.fetchMovimientosLcr(numeroCuenta = numeroCuenta, cantidad = 20)
    } else if (lcrData != null && numeroCuenta == 0) {
        movimientosLcrViewModel.clearMovimientos()
    }

    // State for showing the full-screen dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Línea De Crédito Rotativa",
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
                            tint = com.example.oriencoop_score.ui.theme.amarillo
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
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "")
            }
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "error", color = Color.Red)
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Details section
                    item {
                        lcrData?.data?.forEach { data ->
                            DetallesLcr(
                                NUMEROCUENTA = data.numerocuenta,
                                CUPOAUTORIZADO = "$ ${data.cupoautorizado}",
                                CUPOUTILIZADO = "$ ${data.cupoutilizado}",
                                CUPODISPONIBLE = "$ ${data.cupodisponible}"
                            )
                        } ?: run {
                            Text(
                                text = "No hay datos disponibles",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Movements header
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
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

                    // Movements content
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            MovimientosLcrScreen(movimientosLcrViewModel)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Show the dialog when showAllMovimientosDialog is true
    if (showAllMovimientosDialog) {
        AllMovimientosDialogLcr(
            movimientosLcrViewModel = movimientosLcrViewModel,
            onDismiss = { showAllMovimientosDialog = false }
        )
    }
}

/**
 * Parses the numeroCuenta from the format "10-001-0006819-6" to extract the middle part "0006819"
 * and convert it to an Int (e.g., 6819) by removing leading zeros.
 *
 * @param account The account number string in the format "10-001-0006819-6".
 * @return The parsed numeroCuenta as an Int, or 0 if parsing fails.
 */
fun parseNumeroCuenta(account: String): Int {
    return try {
        val parts = account.split("-")
        if (parts.size == 4) {
            parts[2].toIntOrNull()?.let { num ->
                num.toString().toIntOrNull() ?: 0
            } ?: 0
        } else {
            0
        }
    } catch (e: Exception) {
        0
    }
}

@Composable
fun AllMovimientosDialogLcr(
    movimientosLcrViewModel: MovimientosLcrViewModel,
    onDismiss: () -> Unit
) {
    val movimientosData by movimientosLcrViewModel.movimientosData.collectAsState()
    val isLoading by movimientosLcrViewModel.isLoading.collectAsState()
    val error by movimientosLcrViewModel.error.collectAsState()

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
                            tint = com.example.oriencoop_score.ui.theme.amarillo
                        )
                    }
                    Text(
                        text = "Todos los Movimientos",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }
                HorizontalDivider()

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "error",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    else -> {
                        movimientosData?.data?.let { movimientos ->
                            MovimientosListLcr(movimientos = movimientos)
                        } ?: Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No hay movimientos disponibles")
                        }
                    }
                }
            }
        }
    }
}