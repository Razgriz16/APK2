package com.example.oriencoop_score.view.mis_productos.credito_cuotas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.BottomBar
import com.example.oriencoop_score.view_model.CreditoCuotasViewModel
import com.example.oriencoop_score.view_model.MovimientosCreditosViewModel
import kotlinx.coroutines.launch

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
    val cuentaSeleccionada by creditoCuotasViewModel.cuentaSeleccionada.collectAsState()

    // State to control the expanded Movimientos dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }
    var selectedAccountForDialog by remember { mutableStateOf<Long?>(null) }

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
            Box(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                BottomBar(navController)
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
                // Reemplazamos Column por LazyColumn para hacer la vista scrolleable
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
                        itemsIndexed(data.credito_cuotas) { index, cuenta ->
                            // Cada item se maneja por separado.
                            // Asegúrate de que los componentes dentro (como DetallesCreditoCuotas)
                            // no tengan scroll interno sin restricciones.
                            Column {
                                CreditoCuotaItem(cuenta, cuentaSeleccionada?.NROCUENTA == cuenta.NROCUENTA) {
                                    creditoCuotasViewModel.selectCuenta(cuenta)
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)
                                    }
                                }
                                if (cuentaSeleccionada?.NROCUENTA == cuenta.NROCUENTA) {
                                    DetallesCreditoCuotas(cuenta)

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
                                            selectedAccountForDialog = cuenta.NROCUENTA
                                            showAllMovimientosDialog = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Ver todos los movimientos",
                                                tint = AppTheme.colors.azul
                                            )
                                        }
                                    }
                                    // Si MovimientosCreditosScreen usa scroll internamente,
                                    // también se recomienda asignar un tamaño fijo:
                                    Box(modifier = Modifier.height(300.dp)) {
                                        MovimientosCreditosScreen(
                                            movimientosCreditosViewModel,
                                            selectedAccount = cuenta.NROCUENTA
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
                movimientosCreditosViewModel = movimientosCreditosViewModel,
                selectedAccount = selectedAccountForDialog ?: 0, // Valor por defecto
                onDismiss = { showAllMovimientosDialog = false }
            )
        }
    }
}
@Composable
fun AllMovimientosDialog(
    movimientosCreditosViewModel: MovimientosCreditosViewModel,
    selectedAccount: Long,
    onDismiss: () -> Unit
) {
    val movimientos by movimientosCreditosViewModel.movimientos.collectAsState()
    val isLoading by movimientosCreditosViewModel.isLoading.collectAsState()
    val error by movimientosCreditosViewModel.error.collectAsState()

    val filteredMovimientos = movimientos.filter { it.NROCUENTA == selectedAccount }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Important for full-screen dialog
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Text(
                        "Todos los Movimientos",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = {onDismiss()}){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = com.example.oriencoop_score.ui.theme.amarillo // Use your theme color
                        )
                    }
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
                            Text(
                                text = error ?: "Error desconocido",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            items(filteredMovimientos) { movimiento ->
                                com.example.oriencoop_score.view.mis_productos.credito_cuotas.MovimientosCreditosItem(movimiento = movimiento)
                                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}
