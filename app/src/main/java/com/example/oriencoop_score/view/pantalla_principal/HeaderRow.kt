package com.example.oriencoop_score.view.pantalla_principal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oriencoop_score.R
import com.example.oriencoop_score.utility.SessionManager
import com.example.oriencoop_score.model.Notifications
import com.example.oriencoop_score.navigation.Pantalla
import com.example.oriencoop_score.ui.theme.AppTheme


// Función trata la fila superior de la app
@Composable
fun HeaderRow(
    onMenuClick: () -> Unit = {},
    onLogoClick: () -> Unit = {},
    onAlertClick: () -> Unit = {}
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp + statusBarHeight)
                .background(Color.White),
                //.padding(top = statusBarHeight),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hamburger_menu),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(start = 25.dp)
                        .clickable { onMenuClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.logooriencoop),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { onLogoClick() }
                )
                Image(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 25.dp)
                        .clickable { onAlertClick() }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp)
                .background(Color(0xFFf49600))
        )
    }
}



@Composable
fun DrawerContent(onCloseDrawer: () -> Unit, navController: NavController, drawerWidth: Dp) { // Added drawerWidth parameter

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(drawerWidth)// Set the width to the calculated drawerWidth
            .background(AppTheme.colors.azul)
    ) {
        // Back Arrow (only visible when drawer is open)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onCloseDrawer) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AppTheme.colors.amarillo
                )
            }
        }

        // Profile Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.new_profile), // Replace with your profile icon
                contentDescription = "Profile",
                modifier = Modifier.size(80.dp)
            )
            //Text(text = "Enzo Norambuena", style = AppTheme.typography.titulos)
        }

        // Menu Items (using LazyColumn for scrollable content)
        LazyColumn(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            item {
                DrawerMenuItem(iconId = R.drawable.folders, text = "Mis Datos") { navController.navigate(Pantalla.ClienteInfo.route) }
                //DrawerMenuItem(iconId = R.drawable.clave, text = "Cambiar Clave") { /* TODO: Handle click */ }
                //DrawerMenuItem(iconId = R.drawable.soporte, text = "Contacto con ejecutivo") { /* TODO: Handle click */ }
                //DrawerMenuItem(iconId = R.drawable.ubicacion, text = "Sucursales") { /* TODO: Handle click */ }
                //DrawerMenuItem(iconId = R.drawable.mensaje, text = "Preguntas Frecuentes") { /* TODO: Handle click */ }
                DrawerMenuItem(iconId = R.drawable.salir, text = "Cerrar Sesión", sessionManager = { SessionManager().clearSession() }) {
                    navController.navigate(
                        Pantalla.Login.route
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    iconId: Int,
    text: String,
    sessionManager: (() -> Unit)? = null, // Optional sessionManager lambda
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            //.clickable(onClick = onClick) // Make the row clickable
            .clickable {
                onClick()
                sessionManager?.invoke() // Invoke sessionManager if provided
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = text,
            tint = Color.White, // Adjust color as needed
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = Color.White) // Use MaterialTheme typography
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailScreen(notification: Notifications) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = notification.TITULO, style = MaterialTheme.typography.bodyMedium)
        Text(text = notification.DESCRIPCION, style = MaterialTheme.typography.labelMedium)
        // Optionally include date and time
    }
}


/*
@Composable
fun NotificationDialog(
    onDismiss: () -> Unit
) {
    val viewModel: NotificationViewModel = hiltViewModel()
    val notifications = viewModel.notifications.value // Get the List<Notification>

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
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Notificaciones",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.colors.amarillo
                        )
                    }
                }
                HorizontalDivider()

                LazyColumn {
                    items(notifications) { notification -> // Correct: items takes the List directly
                        NotificationItem(notification, viewModel) // No trailing lambda needed here
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}



@Composable
fun NotificationItem(
    notification: Notifications,  // We still take the Notification as a parameter.
    viewModel: NotificationViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = notification.description,  // Access properties directly from the notification
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Fecha: ${notification.date}  Hora: ${notification.time}", // Access directly
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.clearNotification(notification) // Call clearNotification on the ViewModel
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Clear")
        }
    }
}*/