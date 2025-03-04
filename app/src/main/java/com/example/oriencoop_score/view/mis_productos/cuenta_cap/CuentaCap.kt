package com.example.oriencoop_score.view.mis_productos.cuenta_cap


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
import com.example.oriencoop_score.model.Movimiento
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.CuentaCapViewModel
import com.example.oriencoop_score.view_model.MovimientosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentaCap(
    navController: NavController,

) {
    val cuentaCapViewModel: CuentaCapViewModel = hiltViewModel()
    val movimientosViewModel: MovimientosViewModel = hiltViewModel()

    val cuentaCapData by cuentaCapViewModel.cuentaCapData.collectAsState()
    val movimientos by movimientosViewModel.movimientos.collectAsState()
    val isLoading by movimientosViewModel.isLoading.collectAsState()
    val error by movimientosViewModel.error.collectAsState()

    // State for showing the full-screen dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cuenta capitalización",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = AppTheme.typography.normal.fontSize // Use theme
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = com.example.oriencoop_score.ui.theme.amarillo // Use theme
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

            )
            { BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "") } // Assuming you have a BottomBar composable
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)


        ) {
            Detalles( // Assuming you have a Detalles composable
                accountNumber = "${cuentaCapData?.NROCUENTA}",
                balance = "$ ${cuentaCapData?.SALDOCONTABLE}",
                openingDate = "${cuentaCapData?.FECHAAPERTURA}",
                accountType = "${cuentaCapData?.TIPOCUENTA}"
            )

            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.fillMaxSize()) {

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
                            tint = AppTheme.colors.azul //Consistent
                        )
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error ?: "Error desconocido", color = Color.Red)
                    }
                } else {
                    MovimientosList(movimientos = movimientos)
                }


                Spacer(modifier = Modifier.height(16.dp))

            }
        }

        // Show the dialog when showAllMovimientosDialog is true
        if (showAllMovimientosDialog) {
            AllMovimientosDialog(
                movimientos = movimientos,
                isLoading = isLoading,
                error = error,
                onDismiss = { showAllMovimientosDialog = false }
            )
        }
    }
}

@Composable
fun AllMovimientosDialog(
    movimientos: List<Movimiento>,
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
                    horizontalArrangement = Arrangement.Start, // Align items to the start
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
                            .weight(1f) // Take up remaining space
                            .fillMaxWidth(), // Ensure it fills the weight
                        textAlign = TextAlign.Center
                    )
                    // Add an invisible spacer to balance the row.
                    Spacer(modifier = Modifier.width(48.dp)) // Equal to the IconButton size
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
                    else -> {
                        MovimientosList(movimientos = movimientos)
                    }
                }
            }
        }
    }
}
