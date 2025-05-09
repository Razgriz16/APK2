package com.example.oriencoop_score.view.mis_productos.cuenta_csocial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.CuentaCsocialViewModel
import com.example.oriencoop_score.view_model.MovimientosCsocialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentaCsocial(
    navController: NavController
) {
    val cuentaCsocialViewModel: CuentaCsocialViewModel = hiltViewModel()
    val movimientosViewModel: MovimientosCsocialViewModel = hiltViewModel()
    val cuentaCsocialData by cuentaCsocialViewModel.cuentaCsocialData.collectAsState()

    // State for showing the full-screen dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }

    // Fetch movements when cuentaCsocialData is available
    cuentaCsocialData?.data?.firstOrNull()?.let { cuenta ->
        LaunchedEffect(cuenta.cuenta) {
            movimientosViewModel.fetchMovimientosCsocial(numeroCuenta = cuenta.cuenta)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cuenta capitalización",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
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
            Box{
                BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            cuentaCsocialData?.data?.firstOrNull()?.let { cuenta ->
                Detalles(
                    accountNumber = "${cuenta.cuenta}",
                    balance = "$ ${cuenta.saldoContable}",
                    openingDate = cuenta.fechaapertura,
                    accountType = cuenta.nombre
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                        modifier = Modifier.fillMaxSize()
                        ) {
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
                    MovimientosCsocialScreen(
                        numeroCuenta = cuenta.cuenta,
                        viewModel = movimientosViewModel
                    )
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No se encontraron datos de la cuenta",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Show the dialog when showAllMovimientosDialog is true
        if (showAllMovimientosDialog) {
            AllMovimientosDialog(
                numeroCuenta = cuentaCsocialData?.data?.firstOrNull()?.cuenta ?: 0L,
                viewModel = movimientosViewModel,
                onDismiss = { showAllMovimientosDialog = false }
            )
        }
    }
}

@Composable
fun AllMovimientosDialog(
    numeroCuenta: Long,
    viewModel: MovimientosCsocialViewModel,
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
                            tint = AppTheme.colors.amarillo
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
                MovimientosCsocialScreen(
                    numeroCuenta = numeroCuenta,
                    viewModel = viewModel
                )
            }
        }
    }
}