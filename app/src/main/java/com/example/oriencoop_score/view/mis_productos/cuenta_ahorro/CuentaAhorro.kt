package com.example.oriencoop_score.view.mis_productos.cuenta_ahorro

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.oriencoop_score.model.MovimientosAhorro
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.credito_cuotas.MovimientosCreditosItem
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.CuentaAhorroViewModel
import com.example.oriencoop_score.view_model.MovimientosAhorroViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentaAhorro(
    navController: NavController,
) {
    val cuentaAhorroViewModel: CuentaAhorroViewModel = hiltViewModel()
    val movimientosAhorroViewModel: MovimientosAhorroViewModel = hiltViewModel()

    val cuentaAhorroData by cuentaAhorroViewModel.cuentaAhorroData.collectAsState()
    val isLoading by cuentaAhorroViewModel.isLoading.collectAsState()
    val error by cuentaAhorroViewModel.error.collectAsState()
    val cuentaSeleccionada by cuentaAhorroViewModel.cuentaSeleccionada.collectAsState()
    val movimientosData by movimientosAhorroViewModel.movimientosData.collectAsState()


    var showAllMovimientosDialog by remember { mutableStateOf(false) }
    var selectedAccountForDialog by remember { mutableStateOf<Int?>(null) } // Changed to String for numeroCuentaFormateado
    var movimientosForDialog by remember { mutableStateOf<List<MovimientosAhorro>?>(null) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Cuenta Ahorro",
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
                            tint = com.example.oriencoop_score.ui.theme.amarillo
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Box {
                BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "")
            }
        }
    ) { paddingValues ->
        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            error?.isNotEmpty() == true -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error ?: "Error desconocido", color = Color.Red)
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cuentaAhorroData?.let { data ->
                        itemsIndexed(data.data) { index, cuenta ->
                            val numeroCuentaFormateado = String.format(Locale.ROOT, "%02d%03d%07d%01d", cuenta.codigoSistema, cuenta.codigoSucursal, cuenta.numeroCuenta, cuenta.digitoCuenta)
                            val isSelected = cuentaSeleccionada?.numeroCuenta == cuenta.numeroCuenta
                            Column {
                                CuentaAhorroItem(numeroCuentaFormateado, isSelected) {
                                    if (isSelected) {
                                        // Colapsar: deseleccionar la cuenta sin llamar a la API
                                        cuentaAhorroViewModel.clearCuentaSeleccionada()
                                    } else {
                                        // Desplegar: seleccionar la cuenta y cargar movimientos
                                        cuentaAhorroViewModel.selectCuenta(cuenta)
                                        movimientosAhorroViewModel.fetchMovimientosAhorro(cuenta.numeroCuenta)
                                    }
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)
                                    }
                                }
                                if (isSelected) {
                                    DetallesAhorroScreen(cuenta)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Movimientos",
                                            style = MaterialTheme.typography.titleLarge,
                                            textAlign = TextAlign.Center
                                        )
                                        IconButton(onClick = {
                                            Log.d("CuentaAhorro", "Botón + clicado para cuenta: ${cuenta.numeroCuenta}")
                                            selectedAccountForDialog = cuenta.numeroCuenta
                                            movimientosForDialog = movimientosData?.data
                                            showAllMovimientosDialog = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Ver todos los movimientos",
                                                tint = AppTheme.colors.azul
                                            )
                                        }
                                    }
                                    Box(modifier = Modifier.height(300.dp)) {
                                        MovimientosAhorroScreen(
                                            movimientosAhorroViewModel,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo para ver todos los movimientos
        if (showAllMovimientosDialog) {
            AllMovimientosDialog(
                movimientos = movimientosForDialog,
                selectedAccount = selectedAccountForDialog ?: 0,
                onDismiss = {
                    showAllMovimientosDialog = false
                    //movimientosCreditosViewModel.clearMovimientos()
                }
            )
        }
    }
}

@Composable
fun AllMovimientosDialog(
    movimientos: List<MovimientosAhorro>?,
    selectedAccount: Int,
    onDismiss: () -> Unit
) {
    Log.d("AllMovimientosDialog", "Renderizando diálogo para cuenta $selectedAccount, movimientos: ${movimientos?.size}")

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
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

                movimientos?.let { movimientosList ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(movimientosList) { movimiento ->
                            MovimientosAhorroItem(movimiento = movimiento)
                            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                        }
                    }
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