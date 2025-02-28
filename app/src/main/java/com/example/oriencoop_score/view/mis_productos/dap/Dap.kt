package com.example.oriencoop_score.view.mis_productos.dap

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.DapViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dap(
    navController: NavController,

    ) {
    val dapViewModel: DapViewModel = hiltViewModel()
    val dapData by dapViewModel.dapData.collectAsState()
    val isLoading by dapViewModel.isLoading.collectAsState()
    val error by dapViewModel.error.collectAsState()
    val cuentaSeleccionada by dapViewModel.cuentaSeleccionada.collectAsState()

    // Estado de scroll para la LazyColumn
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Depósito a plazo",
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
                    itemsIndexed(dapData) { index, cuenta ->
                        val isSelected =
                            cuentaSeleccionada?.numeroDeposito == cuenta?.numeroDeposito
                        Column {
                            if (cuenta != null) {
                                DapItem(cuenta, isSelected) {
                                    dapViewModel.selectCuenta(cuenta)
                                    // Animamos el scroll para que el item seleccionado quede en la parte superior
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)

                                    }
                                }
                            }
                            if (isSelected) {
                                // Se muestra la información sin envolver DetallesAhorroScreen en una caja
                                if (cuenta != null) {
                                    DetallesDap(cuenta)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){}
                            }
                        }
                    }
                }
            }
        }
    }
}
