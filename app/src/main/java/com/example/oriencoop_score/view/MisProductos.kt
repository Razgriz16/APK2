package com.example.oriencoop_score.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.R
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view_model.MisProductosViewModel

//*****Pantalla Principal*****

@Composable
fun MisProductos(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {


        ProductsScreen(
            navController = navController,
            onBackClick = { navController.navigate(Pantalla.PantallaPrincipal.route) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onBackClick: () -> Unit,
    navController: NavController,

) {
    val misProductosViewModel: MisProductosViewModel = hiltViewModel()

    val productos by misProductosViewModel.productos.collectAsState()
    val error by misProductosViewModel.error.collectAsState()
    val isLoading by misProductosViewModel.isLoading.collectAsState()

    if (isLoading){
        LoadingScreen()

    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {

                    Text(
                        fontSize = AppTheme.typography.normal.fontSize,
                        text = "Mis Productos",
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
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)

            )
            { BottomBar(navController) }
        }


    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            // Primera fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProductButton(
                    icon = R.drawable.bank,
                    text = "Cuenta Capitalización",
                    onClick = { navController.navigate(Pantalla.CuentaCap.route) },
                    isVisible = productos["CSOCIAL"] ?: false
                )

                ProductButton(
                    icon = R.drawable.piggy_bank,
                    text = "Cuenta De\nahorro",
                    onClick = { navController.navigate(Pantalla.CuentaAhorro.route)},
                    isVisible = productos["AHORRO"] ?: false
                )
            }
            //Fin Primera Fila//

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProductButton(
                    icon = R.drawable.credito_cuotas,
                    text = "Crédito en\ncuotas",
                    onClick = {navController.navigate(Pantalla.CreditoCuotas.route)},
                    isVisible = productos["CREDITO"] ?: false
                )



                ProductButton(
                    icon = R.drawable.lcc,
                    text = "Línea de crédito\nde cuotas",
                    onClick = { navController.navigate(Pantalla.Lcc.route) },
                    isVisible = productos["LCC"] ?: false
                )
            }



            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProductButton(
                    icon = R.drawable.lcr,
                    text = "Línea de crédito\nrotativa",
                    onClick = { /* Handle click */ },
                    isVisible = productos["LCR"] ?: false
                )

                ProductButton(
                    icon = R.drawable.deposito,
                    text = "Depósito a\nplazo",
                    onClick = { /* Handle click */ },
                    isVisible = productos["DEPOSTO"] ?: false
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}






