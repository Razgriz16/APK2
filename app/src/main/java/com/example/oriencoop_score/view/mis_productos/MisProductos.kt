package com.example.oriencoop_score.view.mis_productos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.R
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view.pantalla_principal.LoadingScreen
import com.example.oriencoop_score.view_model.MisProductosViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

@Composable
fun MisProductos(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ProductsScreen(
            navController = navController,
            onBackClick = { navController.navigate(Pantalla.PantallaPrincipal.route) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    onBackClick: () -> Unit
) {
    val misProductosViewModel: MisProductosViewModel = hiltViewModel()
    val productos by misProductosViewModel.productosActivos.collectAsState()
    val error by misProductosViewModel.error.collectAsState()
    val isLoading by misProductosViewModel.isLoading.collectAsState()


    // Mostrar mensaje de error si existe
    error?.let {
        Text(
            text = "Error: $it",
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Productos",
                        fontSize = AppTheme.typography.normal.fontSize,
                        color = AppTheme.colors.negro,
                        textAlign = TextAlign.Left
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
            BottomBar(
                navController = navController,
                currentRoute = navController.currentDestination?.route ?: ""
            )
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Primera fila
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProductButton(
                            icon = R.drawable.cuenta_cap,
                            text = "Cuenta Capitalización",
                            onClick = { navController.navigate(Pantalla.CuentaCap.route) },
                            iconSize = 35.dp
                        )

                        ProductButton(
                            icon = R.drawable.ahorro,
                            text = "Cuenta de\nAhorro",
                            onClick = { navController.navigate(Pantalla.CuentaAhorro.route) },
                            isVisible = productos["AHORRO"] ?: false,
                            iconSize = 35.dp
                        )
                    }

                    // Segunda fila
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProductButton(
                            icon = R.drawable.credito_cuotas_og,
                            text = "Crédito en\nCuotas",
                            onClick = { navController.navigate(Pantalla.CreditoCuotas.route) },
                            isVisible = productos["CREDITO"] ?: false,
                            iconSize = 35.dp
                        )

                        ProductButton(
                            icon = R.drawable.lcc,
                            text = "Línea de Crédito\na Cuotas",
                            onClick = { navController.navigate(Pantalla.Lcc.route) },
                            isVisible = productos["LCC"] ?: false,
                            iconSize = 35.dp
                        )
                    }

                    // Tercera fila
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProductButton(
                            icon = R.drawable.lcr,
                            text = "Línea de Crédito\nRotativa",
                            onClick = { navController.navigate(Pantalla.Lcr.route) },
                            isVisible = productos["LCR"] ?: false,
                            iconSize = 35.dp
                        )

                        ProductButton(
                            icon = R.drawable.deposito_a_plazo,
                            text = "Depósito a\nPlazo",
                            onClick = { navController.navigate(Pantalla.Dap.route) },
                            isVisible = productos["DEPOSITO"]
                                ?: false, // Corregido de DEPOSTO a DEPOSITO
                            iconSize = 35.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}