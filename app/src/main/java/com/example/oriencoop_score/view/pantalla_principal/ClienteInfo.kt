package com.example.oriencoop_score.view.pantalla_principal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view_model.SessionViewModel
import com.example.oriencoop_score.view_model.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteInfo(
    navController: NavController,// Nuevo parámetro para pasar el RUT
    viewModel: ClienteViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    // Observar el estado del ViewModel
    val clienteInfoState = viewModel.clienteInfoState.collectAsState().value
    val rut = sessionViewModel.sessionState.collectAsState().value.userRut

    // Llamar a fetchClienteInfo al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchClienteInfo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis datos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Pantalla.PantallaPrincipal.route) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.colors.amarillo
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.blanco
                )
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(bottom = 16.dp)) {
                BottomBar(
                    navController = navController,
                    currentRoute = navController.currentDestination?.route ?: ""
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.blanco)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            when {
                clienteInfoState.isLoading -> {
                    LoadingScreen()
                }
                clienteInfoState.error != null -> {
                    Text(
                        text = "Error: ${clienteInfoState.error}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                clienteInfoState.userData != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colors.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ClientInfoItem(
                                label = "Nombre:",
                                value = "${clienteInfoState.userData.nombres} ${clienteInfoState.userData.apellidopaterno} ${clienteInfoState.userData.apellidomaterno}"
                            )
                            ClientInfoItem(
                                label = "rut:",
                                value = "$rut"
                            )
                            ClientInfoItem(
                                label = "Email:",
                                value = clienteInfoState.userData.email
                            )
                            ClientInfoItem(
                                label = "Dirección:",
                                value = clienteInfoState.addressData?.calleNumero ?: "No disponible"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientInfoItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.colors.primary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = AppTheme.colors.onSurface,
            fontSize = 18.sp
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppTheme.colors.outline
        )
    }
}


