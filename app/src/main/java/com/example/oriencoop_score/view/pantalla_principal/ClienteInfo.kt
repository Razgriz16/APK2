package com.example.oriencoop_score.view.pantalla_principal

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oriencoop_score.model.ClienteInfoResponse
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.LoginScreen
import com.example.oriencoop_score.view_model.ClienteInfoViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteInfo(
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    val clienteInfoViewModel: ClienteInfoViewModel = hiltViewModel()
    val clienteInfo = clienteInfoViewModel.clienteInfo.value
    val isLoading = clienteInfoViewModel.isLoading.value
    val error = clienteInfoViewModel.error.value



    if (error != null) {
        Text(text = "Error: $error")
    }

    // Apply the custom color scheme
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis datos"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.colors.amarillo
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.blanco, // Consistent with your image
                )
            )
        },
        bottomBar = {
            // Assuming you have a BottomBar composable, consistent with the image.
            Box(modifier = Modifier.padding(bottom = 16.dp))
            {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.blanco) // Use a light background
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (clienteInfo != null) {
                // Client Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colors.surface
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        ClientInfoItem("Nombre:", clienteInfo.NOMBRE)
                        ClientInfoItem("RUT:", clienteInfo.RUTCOMPLETO)
                        ClientInfoItem("Número de teléfono:", clienteInfo.NUMERO)
                        ClientInfoItem("Dirección:", clienteInfo.CALLENUMEROS)
                        ClientInfoItem("Email:", clienteInfo.EMAIL)
                    }
                }
            } else {
                // Error/Loading State
                if (isLoading){
                    LoadingScreen()
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
/*
@Preview(showBackground = true)
@Composable
fun ClientesInfoPreview() {
    val sampleClient = ClienteInfoResponse(
        CALLENUMEROS = "Main Street 123",
        EMAIL = "john.doe@example.com",
        NOMBRE = "John Doe",
        NUMERO = "123-456-7890",
        RUTCOMPLETO = "12345678-9"
    )
    val navController = rememberNavController()

    ClienteInfo(navController = navController, clienteInfo = sampleClient)
}

@Preview(showBackground = true)
@Composable
fun ClientesInfoNullPreview() {
    val navController = rememberNavController()
    ClienteInfo(navController = navController, clienteInfo = null)

}*/