package com.example.oriencoop_score.view.mis_productos.lcr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import com.example.oriencoop_score.model.MovimientosLcr
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.lcr.DetallesLcr
import com.example.oriencoop_score.view.mis_productos.lcr.MovimientosListLcr
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
    val movimientos by movimientosLcrViewModel.movimientoslcr.collectAsState()
    val isLoading by movimientosLcrViewModel.isLoading.collectAsState()
    val error by movimientosLcrViewModel.error.collectAsState()

    // State for showing the full-screen dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Linea De Crédito rotativa",
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
        // Add LazyColumn to enable scrolling for the entire content
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Details section
            item {
                lcrData?.let { data ->
                    DetallesLcr(
                        NUMEROCUENTA = data.NUMEROCUENTA,
                        CUPOAUTORIZADO = "$ ${data.CUPOAUTORIZADO}",
                        CUPOUTILIZADO = "$ ${data.CUPOUTILIZADO}",
                        CUPODISPONIBLE = "$ ${data.CUPODISPONIBLE}"
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
                        .height(IntrinsicSize.Min)
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (error != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = error ?: "Error desconocido", color = Color.Red)
                        }
                    } else {
                        // Use a fixed height container to prevent nested scroll conflicts
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            MovimientosListLcr(movimientos = movimientos)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Show the dialog when showAllMovimientosDialog is true
    if (showAllMovimientosDialog) {
        AllMovimientosDialogLcr(
            movimientos = movimientos,
            isLoading = isLoading,
            error = error,
            onDismiss = { showAllMovimientosDialog = false }
        )
    }
}

@Composable
fun AllMovimientosDialogLcr(
    movimientos: List<MovimientosLcr>,
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
                                text = error ?: "Error desconocido",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    else -> {
                        MovimientosListLcr(movimientos = movimientos)
                    }
                }
            }
        }
    }
}