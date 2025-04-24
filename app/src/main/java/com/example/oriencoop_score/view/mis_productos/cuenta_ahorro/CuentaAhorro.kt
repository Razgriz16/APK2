package com.example.oriencoop_score.view.mis_productos.cuenta_ahorro

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
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.CuentaAhorroViewModel
import com.example.oriencoop_score.view_model.MovimientosAhorroViewModel
import kotlinx.coroutines.launch
/*
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

    // State to control the expanded Movimientos dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }
    var selectedAccountForDialog by remember { mutableStateOf<Long?>(null) }

    // Estado de scroll para la LazyColumn
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
            Box(modifier = Modifier.padding(bottom = 16.dp)) {
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
                // Usamos LazyColumn para que toda la vista sea scrolleable
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
                        itemsIndexed(data.ahorro) { index, cuenta ->
                            val isSelected = cuentaSeleccionada?.NROCUENTA == cuenta.NROCUENTA
                            Column {
                                CuentaAhorroItem(cuenta, isSelected) {
                                    cuentaAhorroViewModel.selectCuenta(cuenta)
                                    // Animamos el scroll para que el item seleccionado quede en la parte superior
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)
                                    }
                                }
                                if (isSelected) {
                                    // Se muestra la información sin envolver DetallesAhorroScreen en una caja
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
                                    // Como MovimientosAhorroScreen contiene scroll, lo envolvemos en un Box con tamaño fijo
                                    Box(modifier = Modifier.height(300.dp)) {
                                        MovimientosAhorroScreen(
                                            movimientosAhorroViewModel,
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
                movimientosAhorroViewModel = movimientosAhorroViewModel,
                selectedAccount = selectedAccountForDialog ?: 0,
                onDismiss = { showAllMovimientosDialog = false }
            )
        }
    }
}


@Composable
fun AllMovimientosDialog(
    movimientosAhorroViewModel: MovimientosAhorroViewModel,
    selectedAccount: Long,
    onDismiss: () -> Unit
) {
    val movimientos by movimientosAhorroViewModel.movimientos.collectAsState()
    val isLoading by movimientosAhorroViewModel.isLoading.collectAsState()
    val error by movimientosAhorroViewModel.error.collectAsState()

    val filteredMovimientos = movimientos.filter { it.NROCUENTA == selectedAccount }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Importante para diálogo a pantalla completa
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
                                MovimientoItem(movimiento = movimiento)
                                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}
*/