package com.example.oriencoop_score.view.mis_productos.credito_cuotas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
import com.example.oriencoop_score.model.MovimientosCreditos
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.CreditoCuotasViewModel
import com.example.oriencoop_score.view_model.MovimientosCreditosViewModel
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditoCuotas(
    navController: NavController
) {
    val creditoCuotasViewModel: CreditoCuotasViewModel = hiltViewModel()
    val movimientosCreditosViewModel: MovimientosCreditosViewModel = hiltViewModel()

    val creditoCuotasData by creditoCuotasViewModel.creditoCuotasData.collectAsState()
    val isLoading by creditoCuotasViewModel.isLoading.collectAsState()
    val error by creditoCuotasViewModel.error.collectAsState()
    val cuotaSeleccionada by creditoCuotasViewModel.cuentaSeleccionada.collectAsState()
    val movimientosData by movimientosCreditosViewModel.movimientosData.collectAsState()

    // State to control the expanded Movimientos dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }
    var selectedAccountForDialog by remember { mutableStateOf<Long?>(null) }
    var movimientosForDialog by remember { mutableStateOf<List<MovimientosCreditos>?>(null) }

    // Estado de scroll para la LazyColumn
    val listState = rememberLazyListState()
    // Scope para lanzar la animación de scroll
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Crédito Cuotas",
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
            Box{
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
                    creditoCuotasData?.let { data ->
                        itemsIndexed(data.data) { index, cuota ->
                            CreditoCuotaItem(cuota, cuotaSeleccionada?.numerocredito == cuota.numerocredito) {
                                Log.d("CreditoCuotas", "Cuenta seleccionada: ${cuota.numerocredito}, cuotaSeleccionada: ${cuotaSeleccionada?.numerocredito}")
                                if (cuotaSeleccionada?.numerocredito == cuota.numerocredito) {
                                    // Colapsar: deseleccionar la cuenta sin llamar a la API
                                    creditoCuotasViewModel.clearCuotaSeleccionada()
                                } else {
                                    // Desplegar: seleccionar la cuenta y cargar movimientos
                                    creditoCuotasViewModel.selectCuota(cuota)
                                    movimientosCreditosViewModel.fetchMovimientosCreditos(cuota.credito)
                                }
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index)
                                }
                            }
                                if (cuotaSeleccionada?.numerocredito == cuota.numerocredito) {
                                    DetallesCreditoCuotas(cuota)

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
                                            Log.d("CreditoCuotas", "Botón + clicado para cuenta: ${cuota.numerocredito}")
                                            selectedAccountForDialog = cuota.credito
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
                                        MovimientosCreditosScreen(
                                            movimientosCreditosViewModel = movimientosCreditosViewModel
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
                selectedAccount = selectedAccountForDialog ?: 0L,
                onDismiss = {
                    showAllMovimientosDialog = false
                    //movimientosCreditosViewModel.clearMovimientos()
                }
            )
        }
    }


@Composable
fun AllMovimientosDialog(
    movimientos: List<MovimientosCreditos>?,
    selectedAccount: Long,
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
                            MovimientosCreditosItem(movimiento = movimiento)
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