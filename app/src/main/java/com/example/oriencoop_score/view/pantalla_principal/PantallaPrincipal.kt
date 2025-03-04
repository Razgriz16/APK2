package com.example.oriencoop_score.view.pantalla_principal
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oriencoop_score.R
import com.example.oriencoop_score.model.Notifications
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.ui.theme.AppTheme
import com.example.oriencoop_score.view_model.MisProductosViewModel
import com.example.oriencoop_score.view_model.NotificationViewModel
import kotlinx.coroutines.delay
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
    val drawerWidth = screenWidth * 0.5f

    val viewModel: NotificationViewModel = hiltViewModel()
    val singleNotification = viewModel.singleNotification
    var showDialog by remember { mutableStateOf(false) }
    var shouldSendNotification by remember { mutableStateOf(false) }

    // LaunchedEffect that triggers when shouldSendNotification changes
    LaunchedEffect(shouldSendNotification) {
        if (shouldSendNotification) {
            delay(1500) // Wait 1.5 seconds
            viewModel.sendNotification(singleNotification)

            // Reset the flag after sending
            shouldSendNotification = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onCloseDrawer = { scope.launch { drawerState.close() } },
                navController = navController,
                drawerWidth = drawerWidth
            )
        }
    ) {
        Scaffold(
            topBar = {
                HeaderRow(
                    onMenuClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                    onAlertClick = {
                        showDialog = true
                        shouldSendNotification = true
                    },
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    BottomBar(navController, currentRoute = navController.currentDestination?.route ?: "")
                }
            },
            content = { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Saldo Section
                    item {
                        Saldo(navController = navController)
                    }

                    // Quick Actions Header
                    item {
                        Text(
                            text = "Acciones rápidas",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    }

                    // Quick Actions
                    item {
                        AccionesRapidas(
                            productos = productos,
                            onProductClick = { route -> navController.navigate(route) }
                        )
                    }

                    // Banner
                    item {
                        Image(
                            painter = painterResource(id = R.drawable.banner),
                            contentDescription = "Banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    // Mindicator Test
                    item {
                        MindicatorTest()
                    }

                }
            }
        )

        // Notification Dialog
        if (showDialog) {
            NotificationDialog(
                viewModel = viewModel,
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaPrincipalPreview() {
    val navController = rememberNavController() // Create a dummy NavController for preview
    PantallaPrincipal(navController)
}
