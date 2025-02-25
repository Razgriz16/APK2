package com.example.oriencoop_score.view.mis_productos.lcc

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
import com.example.oriencoop_score.model.MovimientosLcc
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.LccViewModel
import com.example.oriencoop_score.view_model.MovimientosLccViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lcc(
    navController: NavController
) {
    val lccViewModel: LccViewModel = hiltViewModel()
    val movimientosLccViewModel: MovimientosLccViewModel = hiltViewModel()

    val lccData by lccViewModel.lccData.collectAsState()
    val movimientos by movimientosLccViewModel.movimientoslcc.collectAsState()
    val isLoading by movimientosLccViewModel.isLoading.collectAsState()
    val error by movimientosLccViewModel.error.collectAsState()

    // State for showing the full-screen dialog
    var showAllMovimientosDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Linea De Crédito a cuotas",
                        color = Color.Black,
                        textAlign = TextAlign.Left,
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
            { BottomBar(navController) } // Assuming you have a BottomBar composable
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)


        ) {
            lccData?.let { data ->
                data.lcc.forEach { item ->
                    DetallesLcc( // Assuming you have a Detalles composable
                        accountNumber = item.NROCUENTA,
                        cupoAutorizado = "$ ${item.CUPOAUTORIZADO}",
                        cupoUtilizado = "$ ${item.CUPOUTILIZADO}",
                        cupoDisponible = "$ ${item.CUPODISPONIBLE}"
                    )
                }
            }
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
                    MovimientosListLcc(movimientos = movimientos)
                }


                Spacer(modifier = Modifier.height(16.dp))

            }
        }

        // Show the dialog when showAllMovimientosDialog is true
        if (showAllMovimientosDialog) {
            AllMovimientosDialogLcc(
                movimientos = movimientos,
                isLoading = isLoading,
                error = error,
                onDismiss = { showAllMovimientosDialog = false }
            )
        }
    }
}

@Composable
fun AllMovimientosDialogLcc(
    movimientos: List<MovimientosLcc>,
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Todos los Movimientos",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = com.example.oriencoop_score.ui.theme.amarillo // Use theme
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
                            Text(text = error ?: "Error desconocido", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    else -> {
                        MovimientosListLcc(movimientos = movimientos)
                    }
                }
            }
        }
    }
}
