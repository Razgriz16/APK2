package com.example.oriencoop_score.view.mis_productos.lcc

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.oriencoop_score.model.MovimientosLcc
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view.pantalla_principal.BottomBar
import com.example.oriencoop_score.view_model.FacturasLccViewModel
import com.example.oriencoop_score.view_model.LccViewModel
import com.example.oriencoop_score.view_model.MovimientosLccViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lcc(
    navController: NavController
) {
    val lccViewModel: LccViewModel = hiltViewModel()
    val movimientosLccViewModel: MovimientosLccViewModel = hiltViewModel()
    val FacturasLccViewModel: FacturasLccViewModel = hiltViewModel()

    val lccData by lccViewModel.lccData.collectAsState()
    val movimientos by movimientosLccViewModel.movimientoslcc.collectAsState()
    val isLoading by movimientosLccViewModel.isLoading.collectAsState()
    val error by movimientosLccViewModel.error.collectAsState()
    val facturas by FacturasLccViewModel.facturaslcc.collectAsState()
    var showFacturasDialog by remember { mutableStateOf(false) }

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
            { BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "") } // Assuming you have a BottomBar composable
        }

    ) { paddingValues ->
        LazyColumn(  // Use LazyColumn for scrollable content
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {  // Wrap each section in an item block
                lccData?.let { data ->
                    data.lcc.forEach { item ->
                        DetallesLcc( // Assuming you have a Detalles composable
                            accountNumber = item.numerocuenta,
                            cupoAutorizado = "$ ${item.cupoautorizado}",
                            cupoUtilizado = "$ ${item.cupoutilizado}",
                            cupoDisponible = "$ ${item.cupodisponible}"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showFacturasDialog = true },  // Clickable Card
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), // Add some elevation
                    shape = RoundedCornerShape(8.dp),  // Rounded corners like the image
                    colors = CardDefaults.cardColors(containerColor = Color.White) // White background

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), // Padding inside the Card
                        horizontalArrangement = Arrangement.SpaceBetween, // Space out the text and icon
                        verticalAlignment = Alignment.CenterVertically // Center vertically
                    ) {
                        Text(
                            text = "Facturas",
                            style = MaterialTheme.typography.titleMedium, // Or titleLarge, adjust as needed
                            fontWeight = FontWeight.Bold  // Make the text bold
                        )
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ver todas las facturas",
                            tint = AppTheme.colors.azul
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item { //Movimientos section

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
            }

            item {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) { // Added height
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) { // Added height
                        Text(text = error ?: "Error desconocido", color = Color.Red)
                    }
                } else {
                    Box(modifier = Modifier.height(200.dp)) { // Constrain the height
                        MovimientosListLcc(movimientos = movimientos)
                    }
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

        if (showFacturasDialog) {
            FacturasLccDialog(
                facturas = facturas,
                isLoading = isLoading,
                error = error,
                onDismiss = { showFacturasDialog = false }
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
            // Set up NestedScrollConnection
            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                        // Try to consume scroll delta before child
                        return Offset.Zero // Let the child handle it
                    }

                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        // We're not consuming, let the child handle it
                        return Offset.Zero
                    }

                    override suspend fun onPreFling(available: Velocity): Velocity {
                        return Velocity.Zero // Let child handle fling
                    }

                    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                        return Velocity.Zero // Let child handle fling
                    }
                }
            }


            Column(modifier = Modifier.fillMaxSize().nestedScroll(nestedScrollConnection)) { // Use nestedScroll modifier
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
                // No need for LazyColumn here, as MovimientosListLcc should be its own LazyColumn
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