package com.example.oriencoop_score.view.pantalla_principal

import com.example.oriencoop_score.view_model.MindicatorsViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.R
import com.example.oriencoop_score.di.NetworkModule
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.repository.MindicatorsRepository
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.mis_productos.ProductButton
import com.example.oriencoop_score.view_model.CuentaCsocialViewModel
import retrofit2.Retrofit

// *****Saldo que se muestra*****
@Composable
fun Saldo(navController: NavController) {
/*
    val cuentaCapRepository = CuentaCsocialRepository(LoginManageApi.cuentaCapService) // You might need to adjust constructor if it has dependencies
    val cuentaCapViewModel: CuentaCsocialViewModel = viewModel {  // Using the simple viewModel() overload
        CuentaCsocialViewModel(cuentaCapRepository)
    }
 */
    val cuentaCsocialViewmodel: CuentaCsocialViewModel = hiltViewModel()

    val cuentaCsocialData by cuentaCsocialViewmodel.cuentaCsocialData.collectAsState()
    val isLoadingSaldo by cuentaCsocialViewmodel.isLoading.collectAsState()
    val error by cuentaCsocialViewmodel.error.collectAsState()

    when {
        isLoadingSaldo -> {
            LoadingScreen() // Reemplaza con tu composable de pantalla de carga
        }
        error != null -> {
            Text("Error: $error")
        }
        cuentaCsocialData != null -> {
            cuentaCsocialData?.data?.map { cuenta ->
                SaldoView(saldoContable = cuenta.saldoContable.toString(), navController )

            }
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
            Text(
                text = "Cuenta Capitalización",
                fontSize = AppTheme.typography.normal.fontSize,
                color = AppTheme.colors.azul,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 8.dp), // Padding inferior para el texto
                textAlign = TextAlign.Center
            )
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
                        navController.navigate(Pantalla.CuentaCsocial.route)
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
fun BottomBar(navController: NavController, currentRoute: String) {
    //val context = LocalContext.current
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.height(60.dp)
            .background(Color.White)
            .padding(bottom = navigationBarHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Home
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { navController.navigate(Pantalla.PantallaPrincipal.route) }
        ) {
            Image(
                painter = painterResource(
                    id = if (currentRoute == Pantalla.PantallaPrincipal.route)
                        R.drawable.home_yellow_xml // Your active home icon
                    else R.drawable.home_xml
                ),
                contentDescription = "Inicio",
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = "Inicio",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Black, // Fixed color
                    fontWeight = if (currentRoute == Pantalla.PantallaPrincipal.route)
                        FontWeight.Bold
                    else FontWeight.Normal
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Menu
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { navController.navigate(Pantalla.MisProductos.route) }
                .padding(vertical = 4.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if (currentRoute == Pantalla.MisProductos.route
                        || currentRoute == Pantalla.CuentaCsocial.route
                        || currentRoute == Pantalla.CuentaAhorro.route
                        || currentRoute == Pantalla.CreditoCuotas.route
                        || currentRoute == Pantalla.Lcc.route
                        || currentRoute == Pantalla.Lcr.route
                        || currentRoute == Pantalla.Dap.route)
                        R.drawable.menu_xml_yellow // Your active menu icon
                    else R.drawable.menu_xml
                ),
                contentDescription = "Mis Productos",
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = "Mis Productos",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Black, // Fixed color
                    fontWeight = if (currentRoute == Pantalla.MisProductos.route)
                        FontWeight.Bold
                    else FontWeight.Normal
                ),
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        // Giro
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { /* Add your Giro navigation logic */ }
                .padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.giro_en_linea),
                contentDescription = "Giro",
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = "Giro",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Black, // Fixed color
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Pago
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable { uriHandler.openUri("https://www.oriencoop.cl/express/") }
                .padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pago),
                contentDescription = "Pago en línea",
                modifier = Modifier.size(25.dp)
            )
            Text(
                text = "Pago en línea",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Black, // Fixed color
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun MindicatorTest() {
    // Observe the LiveData as Compose state

    //val mindicatorsRepository = MindicatorsRepository(NetworkModule.mindicatorsService()) // You might need to adjust constructor if it has dependencies
    val mindicator: MindicatorsViewModel = hiltViewModel()

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
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        productosVisibles.forEach { producto ->
            when (producto) {
                "CSOCIAL" -> ProductButton(
                    icon = R.drawable.cuenta_cap,
                    text = "Cuenta Capitalización",
                    onClick = { onProductClick(Pantalla.CuentaCsocial.route) },
                    modifier = Modifier.size(75.dp),
                    textStyle = AppTheme.typography.small
                )

                "AHORRO" -> ProductButton(
                    icon = R.drawable.ahorro,
                    text = "Cuenta De ahorro",
                    onClick = { onProductClick(Pantalla.CuentaAhorro.route) },
                    modifier = Modifier
                        .size(75.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    textStyle = AppTheme.typography.small,
                )

                "CREDITO" -> ProductButton(
                    icon = R.drawable.credito_cuotas_og,
                    text = "Crédito en cuotas",
                    onClick = { onProductClick(Pantalla.CreditoCuotas.route) },
                    modifier = Modifier
                        .size(75.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    textStyle = AppTheme.typography.small
                )

                "LCC" -> ProductButton(
                    icon = R.drawable.lcc,
                    text = "LCC",
                    onClick = { onProductClick(Pantalla.Lcc.route) },
                    modifier = Modifier
                        .size(75.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    textStyle = AppTheme.typography.small
                )

                "LCR" -> ProductButton(
                    icon = R.drawable.lcr,
                    text = "LCR",
                    onClick = { onProductClick(Pantalla.Lcr.route) },
                    modifier = Modifier
                        .size(75.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    textStyle = AppTheme.typography.small
                )

                "DEPOSTO" -> ProductButton(
                    icon = R.drawable.deposito_a_plazo,
                    text = "Depósito a plazo",
                    onClick = { onProductClick(Pantalla.Dap.route) },
                    modifier = Modifier
                        .size(75.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    textStyle = AppTheme.typography.small
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

