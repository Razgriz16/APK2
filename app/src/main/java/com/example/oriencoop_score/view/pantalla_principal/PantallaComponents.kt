package com.example.oriencoop_score.view.pantalla_principal

import MindicatorsViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.R
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.api.ManageMindicatorsApi
import com.example.oriencoop_score.repository.MindicatorsRepository
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.ProductButton
import com.example.oriencoop_score.view_model.CuentaCapViewModel

// *****Saldo que se muestra*****
@Composable
fun Saldo(navController: NavController) {
/*
    val cuentaCapRepository = CuentaCapRepository(LoginManageApi.cuentaCapService) // You might need to adjust constructor if it has dependencies
    val cuentaCapViewModel: CuentaCapViewModel = viewModel {  // Using the simple viewModel() overload
        CuentaCapViewModel(cuentaCapRepository)
    }
 */
    val cuentaCapViewModel: CuentaCapViewModel = hiltViewModel()

    val cuentaCapData by cuentaCapViewModel.cuentaCapData.collectAsState()
    val isLoadingSaldo by cuentaCapViewModel.isLoading.collectAsState()
    val error by cuentaCapViewModel.error.collectAsState()

    when {
        isLoadingSaldo -> {
            LoadingScreen() // Reemplaza con tu composable de pantalla de carga
        }
        error != null -> {
            Text("Error: $error")
        }
        cuentaCapData != null -> {
            // Asumiendo que tu backend provee un valor para "Monto ahorrado a la fecha"
            // Ajusta el nombre de la propiedad según tus datos reales
            SaldoView(saldoContable = cuentaCapData?.SALDOCONTABLE, navController ) // Reemplaza SALDOCONTABLE con la propiedad correcta si es necesario
        }
        else -> {
            // Estado por defecto
            Text("No hay datos disponibles") // o lo que quieras mostrar por defecto
        }
    }
}

@Composable
fun SaldoView(saldoContable: String?, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp), // Padding general del Column
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$",
                    style = AppTheme.typography.titulos.copy(fontWeight = FontWeight.Bold),
                    color = AppTheme.colors.negro
                )
                Text(
                    text = saldoContable ?: "N/A",
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.titulos.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                    color = AppTheme.colors.negro
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Saldo disponible",
                style = AppTheme.typography.normal.copy(fontSize = 12.sp),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            // Línea divisoria y Botón "Ver detalles cuenta" integrados
            Column( // Nuevo Column para contener la línea y el botón
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .border( // Línea divisoria superior APLICADA AL COLUMN CONTENEDOR
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        // Aquí va la lógica de navegación
                        println("Botón 'Ver detalles cuenta' presionado")
                        navController.navigate(Pantalla.CuentaCap.route)
                    }
            ) {
                Row( // Row para el texto del botón
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp), // Padding vertical para el texto dentro del "botón"
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Ver detalles de cuenta",
                        style = AppTheme.typography.normal.copy(fontWeight = FontWeight.Normal, fontSize = 14.sp),
                        color = AppTheme.colors.azul
                    )
                }
            }
        }
    }
}


//*****Barra inferior de la app*****
@Composable
fun BottomBar(navController: NavController)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        // Home
        Image(
            painter = painterResource(id = R.drawable.icon_interface_homeicons),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(start = 25.dp)
                .clickable { navController.navigate(Pantalla.PantallaPrincipal.route) }
        )

        // Menu
        Image(
            painter = painterResource(id = R.drawable.icon_button_menuicon_top),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(start = 25.dp)
                .clickable { navController.navigate(Pantalla.MisProductos.route) }
        )

        // Giro
        Image(
            painter = painterResource(id = R.drawable.icon_arrow_undoicons),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(start = 25.dp)
                .clickable { }
        )

        // Pago

        Image(
            painter = painterResource(id = R.drawable.icon_sign_dollaricons),
            contentDescription = null,

            modifier = Modifier
                .size(60.dp)
                .padding(end = 25.dp)
                .clickable { }
        )

    }
}

@Composable
fun MindicatorTest() {
    // Observe the LiveData as Compose state

    val mindicatorsRepository = MindicatorsRepository(ManageMindicatorsApi.mindicators) // You might need to adjust constructor if it has dependencies
    val mindicator: MindicatorsViewModel = viewModel {  // Using the simple viewModel() overload
        MindicatorsViewModel(mindicatorsRepository)
    }

    val indicadoresState by mindicator.indicadores.collectAsState()
    val isLoading by mindicator.isLoading.collectAsState()

    if (isLoading){
        CircularProgressIndicator()
    }


    // Display the data
    when (val result = indicadoresState) {
        is Result.Success -> {
            val uf = mindicator.getUF()
            val dolar = mindicator.getDolar()
            val euro = mindicator.getEuro()
            val utm = mindicator.getUTM()

            StructuredLayout(uf, dolar, euro, utm)

        }

        is Result.Error -> {
            Text(text = "Error: ${result.exception.message}")
        }

        else -> {
            // Handle other states (e.g., loading or initial state)
        }
    }
}


@Composable //*****Estructura mindicatoros*****//
fun StructuredLayout(uf: String?, dolar: String?, euro: String?, utm: String?) {
    Column (modifier = Modifier.padding(horizontal = 60.dp)){
        Text(modifier= Modifier.fillMaxWidth(), text = "Indicadores Económicos", style = AppTheme.typography.normal, color = AppTheme.colors.azul, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        // First row: UF and Dolar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(text = "Dólar: $dolar", style = AppTheme.typography.normal)
            Text(text = "UF: $uf", style = AppTheme.typography.normal)
        }

        // Second row: Euro and UTM
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Euro: $euro", style = AppTheme.typography.normal)
            Text(text = "UTM: $utm", style = AppTheme.typography.normal)
        }
    }
}

@Composable
fun AccionesRapidas(
    productos: Map<String, Boolean>,
    onProductClick: (String) -> Unit
) {
    // Filtrar los primeros tres productos visibles
    val productosVisibles = productos.filterValues { it }.keys.take(3)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        productosVisibles.forEach { producto ->
            when (producto) {
                "CSOCIAL" -> ProductButton(
                    icon = R.drawable.bank,
                    text = "Cuenta Capitalización",
                    onClick = { onProductClick(Pantalla.CuentaCap.route) },
                    modifier = Modifier.size(70.dp)
                )

                "AHORRO" -> ProductButton(
                    icon = R.drawable.piggy_bank,
                    text = "Cuenta De\nahorro",
                    onClick = { onProductClick(Pantalla.CuentaAhorro.route) },
                    modifier = Modifier
                        .size(70.dp)
                        .border( // Línea divisoria superior APLICADA AL COLUMN CONTENEDOR
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                )

                "CREDITO" -> ProductButton(
                    icon = R.drawable.credito_cuotas,
                    text = "Crédito en\ncuotas",
                    onClick = { onProductClick(Pantalla.CreditoCuotas.route) },
                    modifier = Modifier
                        .size(70.dp)
                        .border( // Línea divisoria superior APLICADA AL COLUMN CONTENEDOR
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                )

                "LCC" -> ProductButton(
                    icon = R.drawable.lcc,
                    text = "Línea de crédito\nde cuotas",
                    onClick = { onProductClick(Pantalla.Lcc.route) },
                    modifier = Modifier
                        .size(70.dp)
                        .border( // Línea divisoria superior APLICADA AL COLUMN CONTENEDOR
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                )

                "LCR" -> ProductButton(
                    icon = R.drawable.lcr,
                    text = "Línea de crédito\nrotativa",
                    onClick = { onProductClick(Pantalla.Lcr.route) },
                    modifier = Modifier
                        .size(70.dp)
                        .border( // Línea divisoria superior APLICADA AL COLUMN CONTENEDOR
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                )

                "DEPOSTO" -> ProductButton(
                    icon = R.drawable.deposito,
                    text = "Depósito a\nplazo",
                    onClick = {onProductClick(Pantalla.Dap.route) },
                    modifier = Modifier
                        .size(70.dp)
                        .border( // Línea divisoria superior APLICADA AL COLUMN CONTENEDOR
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(exception: Throwable) {
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Error: ${exception.message}", color = Color.Red)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

