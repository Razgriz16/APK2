package com.example.oriencoop_score.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oriencoop_score.R
import com.example.oriencoop_score.view_model.MisProductosViewModel
import kotlinx.coroutines.launch


//*****Pantalla Principal*****
@Composable
fun PantallaPrincipal(
    navController: NavController
) {
    val misProductosViewModel: MisProductosViewModel = hiltViewModel()

    val productos by misProductosViewModel.productos.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.5f // 50% of screen width

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onCloseDrawer = { scope.launch { drawerState.close() } },
                navController = navController,
                drawerWidth = drawerWidth
            )
        },
        gesturesEnabled = true // Enable swipe gestures to open/close drawer (optional)
    ) { // Enable swipe gestures to open/close drawer
        Scaffold(
            topBar = {
                HeaderRow(
                    onMenuClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)

                )
                { BottomBar(navController) } // Assuming you have a BottomBar composable
            },
            content = { padding -> // padding values are provided by Scaffold to avoid overlapping with topBar and bottomBar
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(padding), // Apply padding to the Column content
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Saldo
                    Saldo(navController = navController)

                    Text(text  = "Acciones rápidas", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color.Black)

                    AccionesRapidas(
                        productos = productos,
                        onProductClick = { route -> navController.navigate(route) }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.banner),
                        contentDescription = "Banner",
                        modifier = Modifier
                            .padding(16.dp)
                    )

                    MindicatorTest()

                    Spacer(modifier = Modifier.weight(1f))

                }
            }
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaPrincipalPreview() {
    val navController = rememberNavController() // Create a dummy NavController for preview
    PantallaPrincipal(navController)
}
